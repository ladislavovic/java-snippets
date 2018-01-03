package cz.kul.snippets.java._23_nio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * NOTE:
 * <ul>
 * <li>it means Non-blocking IO</li>
 * <li>from Java 1.4</li>
 * <li>Alternative for Java IO and Java Networking</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.5
 */
public class Main_JavaNIO {

    public static void main(String[] args) throws IOException {
        buffers();
        channels();
        fileChannels();
        path();
    }

    private static void path() {
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
        //   * RESULT: java.io.File is no deprecated, but java.nio.file package is the preffered for new code

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

    private static void fileChannels() throws IOException {
        // NOTE: file channel can not be non-blocking

        getReadableFileChannel("foo", "bar");
        readFromFileChannel();
        writeToFileChannel();

    }

    private static void writeToFileChannel() throws IOException {
        String data = "This is my file data";
        String name = "readFromChannel.txt";
        try (FileChannel channel = getWriteableFileChannel(name)) {
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.put(data.getBytes());
            buf.flip();
            while (buf.hasRemaining()) {
                channel.write(buf);
            }
        }

        try (FileChannel channel = getReadableFileChannel(name, null)) {
            ByteBuffer buf = ByteBuffer.allocate(48);
            channel.read(buf);
            buf.flip();
            String str = bufToStr(buf);
            assertEquals(data, str);
        }
    }

    private static void readFromFileChannel() throws IOException {
        String data = "This is my file data";
        String name = "readFromChannel.txt";
        try (FileChannel channel = getReadableFileChannel(name, data)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();
            byte[] output = new byte[buffer.limit()];
            buffer.get(output);
            String str = new String(output);
            assertEquals(data, str);
        }
    }

    private static FileChannel getReadableFileChannel(String filename, String data) throws IOException {
        // You can get it via: file streams, RandomAccessFile, ...

        // Does FileChannel.close() close the underlying stream?
        // 
        // The answer is 'yes' but there's nothing in the Javadoc that actually says so. 
        // The reason is that FileChannel itself is an abstract class, and its concrete 
        // implementation provides the implCloseChannel() method, which closes 
        // the underlying FD. However due to that architecture and the fact that 
        // implCloseChannel() is protected, this doesn't get documented.

        String tmpdir = System.getProperty("java.io.tmpdir");
        String path = tmpdir + "/" + filename;
        if (data != null) {
            try (FileWriter fw = new FileWriter(path)) {
                fw.write(data);
                fw.flush();
            }
        }
        @SuppressWarnings("resource") // Closed during channel close
        FileInputStream fis = new FileInputStream(path);
        FileChannel channel = fis.getChannel();
        return channel;
    }

    private static FileChannel getWriteableFileChannel(String filename) throws IOException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        String path = tmpdir + "/" + filename;
        FileOutputStream fos = new FileOutputStream(path);
        FileChannel channel = fos.getChannel();
        return channel;
    }

    private static void channels() throws IOException {

        // Read from the channel
        String data = "This is data in the channel";
        ByteChannel channel = getByteChannel(data);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        StringBuilder output = new StringBuilder();
        int bytesRead = channel.read(buffer);
        while (bytesRead != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                output.append((char) buffer.get());
            }
            buffer.clear();
            bytesRead = channel.read(buffer);
        }
        channel.close();
        assertEquals(data, output.toString());
    }

    private static void buffers() {
        // Allocation
        ByteBuffer buffer = ByteBuffer.allocate(8);
        assertEquals(0, buffer.position());
        assertEquals(8, buffer.limit());
        assertEquals(8, buffer.capacity());

        // Putting data
        // You can put data in the buffer, it chnages position
        byte[] input = { 10, 20, 30 };
        buffer.put(input);
        assertEquals(3, buffer.position());

        // flip()
        // It "flips" buffer from writing mode into reading mode we can say.
        // It set limit to current position and position set to 0.
        buffer.flip();
        assertEquals(0, buffer.position());
        assertEquals(3, buffer.limit());

        // get()
        // it reads from the buffer
        byte[] output = new byte[3];
        buffer.get(output);
        assertEquals(3, buffer.position());
        assertArrayEquals(input, output);
    }

    private static ByteChannel getByteChannel(String data) throws IOException {
        FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
        Path file = Files.createFile(fs.getPath("file.txt"));
        Files.write(file, data.getBytes(StandardCharsets.UTF_8));
        SeekableByteChannel channel = Files.newByteChannel(file);
        return channel;
    }

    private static String bufToStr(ByteBuffer buffer) {
        byte[] output = new byte[buffer.limit()];
        buffer.get(output);
        String str = new String(output);
        return str;
    }

}
