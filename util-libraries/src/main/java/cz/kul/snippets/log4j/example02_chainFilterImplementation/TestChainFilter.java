package cz.kul.snippets.log4j.example02_chainFilterImplementation;

import cz.kul.snippets.MemmoryAppender;
import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.ThreadUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import java.util.Properties;

public class TestChainFilter extends SnippetsTest {

    @After
    public void after() {
        reconfigureLog4jByStandardPropertyFile();
    }

    @Test
    public void test() {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "ALL, MEMMORY_APPENDER");
        props.setProperty("log4j.appender.MEMMORY_APPENDER", MemmoryAppender.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01", ChainFilter.class.getName());
        reconfigureLog4jByProperties(props);
 
        ThreadUtils.sleep(1000); // wait until the filter is fully inicialized
        
        Logger logger = Logger.getLogger(this.getClass());
        logger.info("_A_"); // removed by one filter
        logger.info("_B_"); // removed by another filter
        logger.info("_C_"); // go through all filters and then logged

        assertMessageCountInLog(0, "_A_");
        assertMessageCountInLog(0, "_B_");
        assertMessageCountInLog(1, "_C_");
    }

}
