package cz.kul.snippets.log4j.example03_loggingDuringLog4jInit;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class MyFilter extends Filter {

    public MyFilter() {
        Logger logger = Logger.getRootLogger();
        logger.info("Filter is creating");
    }

    @Override
    public int decide(LoggingEvent event) {
        return NEUTRAL;
    }
    
}
