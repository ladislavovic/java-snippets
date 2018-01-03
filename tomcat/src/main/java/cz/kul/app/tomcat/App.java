package cz.kul.app.tomcat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * Hello world!
 *
 */
public class App {

    private static String RUNTIME = "c:\\tmp\\runtime";
    private static String TEST_LOGGIGN = "c:\\tmp\\runtime\\testLogging";

    public static void main(String[] args) throws Exception {
        testLogging();
    }

    private static void testLogging() throws Exception {
        //        buildAgent();
        //        deployAgent();

        clear();
        copyTomcat();
        runTomcat();
        buildAgent();
        deployAgent();
        //                stopTomcat();
    }

    private static void deployAgent() throws IOException {
        File deployDir = new File(RUNTIME + "\\testLogging\\tomcat\\webapps");

        String currDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File parentFile = (new File(currDir)).getParentFile();
        File war = new File(parentFile.getAbsolutePath() + "/tomcat-agent/target/tomcat-agent.war");
        FileUtils.copyFileToDirectory(war, deployDir);
    }

    private static void buildAgent() throws IOException, InterruptedException {
        // NOTE can not execute mvn install directly, must via cmd.exe. Why?
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C mvn install");
        String currDir = Paths.get(".").toAbsolutePath().normalize().toString();
        File parentFile = (new File(currDir)).getParentFile();
        File wd = new File(parentFile.getAbsolutePath() + "/tomcat-agent");

        pb.directory(wd);
        pb.redirectErrorStream(true);
        Map<String, String> env = pb.environment();
        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();
    }

    private static void clear() throws IOException {
        File runtimeDir = new File(RUNTIME);
        if (!runtimeDir.exists()) {
            runtimeDir.mkdir();
            return;
        }
        FileUtils.deleteDirectory(runtimeDir);
        runtimeDir.mkdir();

        // prepare
        File testLogging = new File(RUNTIME + "\\testLogging");
        testLogging.mkdir();
    }

    private static void copyTomcat() throws IOException {
        File src = new File("./tomcat8");
        File dest = new File(RUNTIME + "\\testLogging\\tomcat");
        FileUtils.copyDirectory(src, dest);
    }

    private static void runTomcat() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(RUNTIME + "/testLogging/tomcat/bin/startup.bat");
        pb.redirectErrorStream(true);
        Map<String, String> env = pb.environment();
        env.put("CATALINA_HOME", RUNTIME + "\\testLogging\\tomcat");
        //        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();

        // It must sleep because startup.bat execute another script and quits. The script really startup
        // the tomcat, but java do not wait for it. So the sleep is here. It would be good to check
        // if tomcat started already.
        Thread.sleep(3000);
        //        int errCode = p.waitFor();
        //        System.out.println("ErrorCode: " + errCode);
        //        output(p.getInputStream());
    }

    private static void stopTomcat() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(RUNTIME + "/testLogging/tomcat/bin/shutdown.bat");
        pb.redirectErrorStream(true);
        Map<String, String> env = pb.environment();
        env.put("CATALINA_HOME", RUNTIME + "\\testLogging\\tomcat");
        Process p = pb.start();
        p.waitFor();
        //        int errCode = p.waitFor();
        //        System.out.println("ErrorCode: " + errCode);
        //        output(p.getInputStream());
    }

    private static void output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            br.close();
        }
    }

}
