package cz.kul.snippets.java.example19_nio;

import com.google.common.collect.Lists;
import cz.kul.snippets.FilesystemHelper;
import cz.kul.snippets.SnippetsTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * NOTE:
 * <ul>
 * <li>NIO = Non-blocking IO</li>
 * <li>from Java 1.4</li>
 * <li>Alternative for Java IO and Java Networking</li>
 * </ul>
 * 
 * NIO TODO: how to create file?;
 */
public class IntroductionToNIO extends SnippetsTest {

    @Test
    public void channels() throws IOException {
        /*
          They are similar to IO Streams with a few differences
            * you can read and write to a Channel. Streams are typically one-way.
            * can be read and write asynchronously
        */

        // Prepare a file
        FilesystemHelper filesystemHelper = getFilesystemHelper();
        File randomDir = filesystemHelper.createRandomDir();
        Path path = Paths.get(randomDir.getAbsolutePath(), "file1.txt");
        Files.write(path, "Monica".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

        // Read from the channel to the buffer
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buf = ByteBuffer.allocate(48);
        channel.read(buf);
        assertEquals("Monica", (new String(buf.array(), StandardCharsets.UTF_8)).trim());
    }

    @Test
    public void buffers() {
        // Buffer is just wrapped part of the memmoty with several helper methods.
        // It has three basic properties:
        //  * capacity - just the size of the buffer
        //  * position - when you read or write, it starts from the position. It is automatically
        //               increased when you read/write
        //  * limit - the limit how much data can read or write. It is the position, not count! For example
        //            if the position position is 0 and limit is 2 then it reads items 0 and 1. 2 nots.
        //            So the limit is on the first position which is not read / written.  
        
        // Allocation
        ByteBuffer buffer = ByteBuffer.allocate(8);
        assertEquals(0, buffer.position());
        assertEquals(8, buffer.limit());
        assertEquals(8, buffer.capacity());
        
        byte[] input = {10, 20, 30};

        // Putting data
        // You can put data in the buffer, it chnages position
        {
            buffer.put(input);
            assertEquals(3, buffer.position());
            assertEquals(8, buffer.limit());
            assertEquals(8, buffer.capacity());
        }

        // flip()
        // It set position and limit for reading written data.
        {
            buffer.flip();
            assertEquals(0, buffer.position());
            assertEquals(3, buffer.limit());
            assertEquals(8, buffer.capacity());
        }
        
        // get()
        // it reads from the buffer
        {
            byte[] output = new byte[3];
            buffer.get(output);
            assertArrayEquals(input, output);
            assertEquals(3, buffer.position());
            assertEquals(3, buffer.limit());
            assertEquals(8, buffer.capacity());
        }
        
        // clear()
        // Clears this buffer. The position is set to zero, the limit
        // is set to the capacity, and the mark is discarded.
        {
            buffer.clear();
        }
        
        // compact()
        // Copy the data between position and limit to the start.
        // Set position after the last "moved" item and limit to the
        // end of the buffer. So the buffer is ready for another put.
        {
            buffer.put(new byte[] {1, 2, 3, 4, 5});
            assertEquals(5, buffer.position());
            assertEquals(8, buffer.limit());

            buffer.flip();
            assertEquals(0, buffer.position());
            assertEquals(5, buffer.limit());

            byte[] out = new byte[3];
            buffer.get(out);
            assertEquals(3, buffer.position());
            assertEquals(5, buffer.limit());
            assertArrayEquals(new byte[] {1, 2, 3}, out);

            buffer.compact();
            assertEquals(2, buffer.position());
            assertEquals(8, buffer.limit());

            buffer.put(new byte[] {11, 12});
            assertEquals(4, buffer.position());
            assertEquals(8, buffer.limit());
            
            buffer.flip();
            assertEquals(0, buffer.position());
            assertEquals(4, buffer.limit());
            
            byte[] out2 = new byte[4];
            buffer.get(out2);
            assertArrayEquals(new byte[] {4, 5, 11, 12}, out2);
            assertEquals(4, buffer.position());
            assertEquals(4, buffer.limit());
        }
    }
    
    @Test
    public void fileChannel() throws IOException {
        // It is and alternative to standard Java IO. Notes:
        //   * can not be set to non-blocking mode. It always runs in blocking mode
        
        // Create a file
        FilesystemHelper filesystemHelper = getFilesystemHelper();
        File randomDir = filesystemHelper.createRandomDir();
        Path path = Paths.get(randomDir.getAbsolutePath(), "file1.txt");
        Files.write(path, "Monica".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        
        // Read a file
        RandomAccessFile aFile = new RandomAccessFile(path.toAbsolutePath().toString(), "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        inChannel.read(buffer);
        buffer.flip();
        byte[] readed = new byte[6];
        buffer.get(readed);
        assertEquals("Monica", (new String(readed, StandardCharsets.UTF_8)));
        
        // Write to the file
        buffer.clear();
        buffer.put("Rachel".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        while (buffer.hasRemaining()) {
            inChannel.write(buffer);
        }
        assertEquals("MonicaRachel", new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        
        // Close
        inChannel.close();
    }
    
    @Test
    public void asynchronousFileRead() throws IOException, InterruptedException {
        FilesystemHelper filesystemHelper = getFilesystemHelper();
        File randomDir = filesystemHelper.createRandomDir();
        Path path = Paths.get(randomDir.getAbsolutePath(), "file1.txt");
        Files.write(path, "Monica".getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        Future<Integer> future = fileChannel.read(buffer, 0);
        while (!future.isDone()) {
            Thread.sleep(100);
        }
        buffer.flip();
        assertEquals("Monica", (new String(buffer.array(), StandardCharsets.UTF_8)).trim());
    }
    
    @Test
    public void path() {
        // NOTES:
        //   * it locate file on the filesystem
        //   * it is an alternative to java.io.File
        //   * immutable
        // Comparison with "old" java.io.File:
        //   * some operations in old api (createDirectory, deleteFile, ...) returns boolean. True for success and false
        //     for fail. But you do no know why it failed.
        //   * FileNotFoundException from old api is throwen in many cases: when file does not exists, when you do not
        //     have permission, when symbolic links are in a cycle, ...
        //   * with FileSystemProvider from new API, you can simply mock file operations during testing
        //   * RESULT: java.io.File is not deprecated, but java.nio.file package is the preffered for the new code

        // Create absolute path
        {
            Path absolutePath = Paths.get("/home/kul/myproject");
        }

        // Create relative path and convert it to absolute one
        {
            Path relative = Paths.get("src/file1.txt");
            Path resolved = Paths.get("/tmp").resolve(relative); // NOTE does not matter if the absolute path ends with slash or not
            assertEquals(Paths.get("/tmp/src/file1.txt"), resolved);
        }

        // Relativize path. It can create relative path between two paths.
        {
            Path path1 = Paths.get("/tmp");
            Path relative = path1.relativize(Paths.get("/tmp/a/b/file1.txt"));
            assertEquals(Paths.get("a/b/file1.txt"), relative);
        }
    }

    @Test
    public void pathElements() {
        assertEquals(Lists.newArrayList(), toElements("/"));
        assertEquals(Lists.newArrayList("foo"), toElements("/foo"));
        assertEquals(Lists.newArrayList("foo", "bar"), toElements("/foo/bar"));

        assertEquals(Lists.newArrayList("foo"), toElements("foo"));
        assertEquals(Lists.newArrayList("foo", "bar"), toElements("foo/bar"));
    }

    private List<String> toElements(String path) {
        return Lists.newArrayList(Paths.get(path).iterator()).stream()
            .map(Path::toString)
            .collect(Collectors.toList());
    }
    
}
