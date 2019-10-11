package cz.kul.snippets.log4j.example03_loggingDuringLog4jInit;

import cz.kul.snippets.MemmoryAppender;
import cz.kul.snippets.SnippetsTest;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Properties;

public class TestLoggingDuringLog4jInit extends SnippetsTest {
    
    @Test
    public void test() {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "INFO, MEMMORY_APPENDER");
        props.setProperty("log4j.appender.MEMMORY_APPENDER", MemmoryAppender.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01", MyFilter.class.getName());
//        reconfigureLog4jByProperties(props);

        Logger logger = Logger.getLogger(this.getClass());
        logger.info("_A_");

        assertMessageCountInLog(1, "_A_");
    }
    
    
    
}
