package cz.kul.snippets.log4j.example01_filter;

import cz.kul.snippets.MemmoryAppender;
import cz.kul.snippets.SnippetsTest;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.StringMatchFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class TestFilter extends SnippetsTest {

    @Before
    public void log4jReset() {
        resetLog4JConfiguration();
    }

    @After
    public void log4jReconfigure() {
        reconfigureLog4jByStandardPropertyFile();
    }

    @Test
    public void customFilter() {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "ALL, A1, MEMMORY_APPENDER");
        props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
        props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.SimpleLayout");
        props.setProperty("log4j.appender.A1.filter.01", AllowMessagesStartingWithA.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER", MemmoryAppender.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01", AllowMessagesStartingWithA.class.getName());
        reconfigureLog4jByProperties(props);

        Logger logger = Logger.getLogger(this.getClass());
        logger.info("foo");
        logger.info("ahoj");

        assertMessageCountInLog(0, "foo");
        assertMessageCountInLog(1, "ahoj");
    }
    
    @Test
    public void filterChain() {
        Properties props = new Properties();
        props.setProperty("log4j.rootLogger", "ALL, MEMMORY_APPENDER");
        props.setProperty("log4j.appender.MEMMORY_APPENDER", MemmoryAppender.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01", StringMatchFilter.class.getName());
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01.stringToMatch", "a");
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.01.acceptOnMatch", "false");
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.02.stringToMatch", "b");
        props.setProperty("log4j.appender.MEMMORY_APPENDER.filter.02.acceptOnMatch", "true");
        reconfigureLog4jByProperties(props);

        Logger logger = Logger.getLogger(this.getClass());
        logger.info("___ a ___"); // denied by filter 01
        logger.info("___ b ___"); // accepted by filter 02
        logger.info("___ c ___"); // go through all filters and then logged

        assertMessageCountInLog(0, "___ a ___");
        assertMessageCountInLog(1, "___ b ___");
        assertMessageCountInLog(1, "___ c ___");
    }

    public static class AllowMessagesStartingWithA extends Filter {

        @Override
        public int decide(LoggingEvent event) {
            String msg = event.getRenderedMessage();
            if (msg == null) return NEUTRAL;
            return msg.matches("[aA].*") ? ACCEPT : DENY;
        }
    }


}
