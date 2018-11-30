package cz.kul.snippets.java.example18_jaas;

import cz.kul.snippets.FilesystemHelper;
import cz.kul.snippets.SnippetsTest;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.*;

public class TestJAAS extends SnippetsTest {

    private static final String CFG_FILE = "sample_jaas.config";
    private static final String RESOURCE_CFG_FILE = "example18/sample_jaas.config";

    @BeforeClass
    public static void init() throws IOException {
        createCfgFile();
    }

    private static void createCfgFile() throws IOException {
        File jassCfgFile = new File(String.format("%s/%s", FilesystemHelper.getInstance().createRandomDir().getPath(), CFG_FILE));
        ClassLoader classLoader = TestJAAS.class.getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(RESOURCE_CFG_FILE);
             FileOutputStream output = new FileOutputStream(jassCfgFile)) {
            IOUtils.copy(input, output);
        }
        System.setProperty("java.security.auth.login.config", jassCfgFile.getPath());
    }

    @Test
    public void testCorrectAuthentication() throws LoginException {
        LoginContext lc = new LoginContext("Sample", new SampleCallbackHandler("ladislav", "butter"));
        lc.login();
        assertEquals("ladislav", lc.getSubject().getPrincipals().iterator().next().getName());
    }

    @Test(expected = LoginException.class)
    public void testFailedAuthentication() throws LoginException {
        LoginContext lc = new LoginContext("Sample", new SampleCallbackHandler("ladislav", "incorrectPassword"));
        lc.login();
    }

}


