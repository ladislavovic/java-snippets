package cz.kul.snippets.java.example24_executeProcess;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.StreamGobbler;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestExecuteProcess extends SnippetsTest {

    private static Logger LOGGER = LoggerFactory.getLogger(TestExecuteProcess.class);

    @Test
    public void processIsAsynchronous() {
        long time = measureTime(() -> {
            execProcess(
                    "java -cp target/classes cz.kul.snippets.java.example24_executeProcess.app.Pause3s",
                    new String[0],
                    null);
        });
        Assert.assertTrue(time < 1000);
    }

    @Test
    public void waitForProcessEnd() {
        long time = measureTime(() -> {
            Process p = execProcess(
                    "java -cp target/classes cz.kul.snippets.java.example24_executeProcess.app.Pause3s",
                    new String[0],
                    null);
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        });
        Assert.assertTrue(time > 3000);
    }

    @Test
    public void logProcessOutput() throws InterruptedException {
        Process process = executeProcessAndRedirectOutput(
                "java -cp target/classes cz.kul.snippets.java.example24_executeProcess.app.Printer",
                new String[0],
                null,
                LOGGER);
        process.waitFor();
        assertMessageInLog("I am Printer");
    }

    @Test
    public void setProcessEnvironment() throws InterruptedException {
        String[] env = {"ENV_VAR_1=envVal"};
        Process process = executeProcessAndRedirectOutput(
                "java -cp target/classes cz.kul.snippets.java.example24_executeProcess.app.EnvVarPrinter",
                env,
                null,
                LOGGER);
        process.waitFor();
        assertMessageInLog("envVal");
    }

    private Process execProcess(String cmd, String[] env, File workingDirectory) {
        try {
            Runtime rt = Runtime.getRuntime();
            return rt.exec(cmd, env, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Process execProcessAndRedirectOutput(String cmd, String[] env, File workingDirectory, Logger logger) {
        Process p = execProcess(cmd, env, workingDirectory);
        StreamGobbler out = new StreamGobbler(p.getInputStream(), "OUT", logger);
        StreamGobbler err = new StreamGobbler(p.getErrorStream(), "ERR", logger);
        out.start();
        err.start();
        return p;
    }

}

