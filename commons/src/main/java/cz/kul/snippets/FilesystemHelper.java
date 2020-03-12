package cz.kul.snippets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;

public class FilesystemHelper {

    private static String DEFAULT_ROOT_DIR = "/var/javasnippets";

    private String rootDir;

    public static FilesystemHelper getInstance() {
        return getInstance(DEFAULT_ROOT_DIR);
    }

    public static FilesystemHelper getInstance(String rootDir) {
        return new FilesystemHelper(rootDir);
    }

    private FilesystemHelper(String rootDir) {
        this.rootDir = rootDir;
    }

    public String createDirPath(String dir) {
        return rootDir + "/" + dir;
    }

    public String createRandomDirPath() {
        int PATH_LEN = 8;
        String random = RandomStringUtils.randomAlphabetic(PATH_LEN);
        return createDirPath(random);
    }

    public File createDir(String dir) {
        return createDirInternal(new File(createDirPath(dir)));
    }

    public File createRandomDir() {
        return createDirInternal(new File(createRandomDirPath()));
    }

    private File createDirInternal(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
            dir.mkdirs();
            return dir;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
