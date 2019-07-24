package cz.kul.snippets.aspectj.example01_adviceOnPartucularMethod;

import org.apache.log4j.Logger;

public aspect ParticularMethodAspect {

    public static String LOG_MSG = "particularMethod";

    private final static Logger logger = Logger.getLogger(ParticularMethodAspect.class);

    // NOTE: you have to write whole class name including packages, without that
    //       I had a compilation error
    pointcut particularMethod() :
            call(void cz.kul.snippets.aspectj.commons.Account.withdraw(int));

    before(): particularMethod() {
        logger.info(LOG_MSG);
    }

}
