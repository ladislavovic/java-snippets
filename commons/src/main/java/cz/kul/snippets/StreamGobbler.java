package cz.kul.snippets;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    InputStream is;
    String prefix;
    Logger logger;

    public StreamGobbler(InputStream is, String prefix, Logger logger) {
        this.is = is;
        this.prefix = prefix;
        this.logger = logger;
        this.setDaemon(true);
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.info(prefix + " > " + line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
