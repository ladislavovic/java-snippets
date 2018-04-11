package cz.kul.snippets.aspectj._01_pointcuts.aspects;

import org.apache.log4j.Logger;

public aspect AspectParticularMethod {

    public static String LOG_MSG = "particularMethod";

    private final static Logger logger = Logger.getLogger(AspectParticularMethod.class);

    pointcut particularMethod() :
            call(void cz.kul.snippets.aspectj._01_pointcuts.Account.withdraw(int));

    before(): particularMethod() {
        logger.info(LOG_MSG);
    }

}
