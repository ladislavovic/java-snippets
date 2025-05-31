package cz.kul.snippets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class FilesystemHelper
{

    private static String DEFAULT_ROOT_DIR = "/var/javasnippets";

    private String rootDir;

    public static FilesystemHelper getInstance()
    {
        return getInstance(DEFAULT_ROOT_DIR);
    }

    public static FilesystemHelper getInstance(String rootDir)
    {
        return new FilesystemHelper(rootDir);
    }

    private FilesystemHelper(String rootDir)
    {
        this.rootDir = rootDir;
    }

    public String createDirPath(String dir)
    {
        return rootDir + "/" + dir;
    }

    public String createRandomDirPath()
    {
        int PATH_LEN = 8;
        String random = Random.createRandomString(PATH_LEN);
        return createDirPath(random);
    }

    public File createDir(String dir)
    {
        return createDirInternal(new File(createDirPath(dir)));
    }

    public File createRandomDir()
    {
        return createDirInternal(new File(createRandomDirPath()));
    }

    private File createDirInternal(File dir)
    {
        try {
            deleteDirectory(dir);
            dir.mkdirs();
            return dir;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void deleteDirectory(File dir) throws IOException
    {
        if (dir.exists()) {
            // Walk through the file tree and delete children before parents. Works recursivelly
            try (Stream<Path> walk = Files.walk(dir.toPath())) {
                walk.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete " + p, e);
                        }
                    });
            }
        }
    }

}
