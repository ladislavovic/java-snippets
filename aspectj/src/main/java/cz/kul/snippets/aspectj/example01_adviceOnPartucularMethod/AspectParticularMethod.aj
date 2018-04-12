package cz.kul.snippets.aspectj.example01_adviceOnPartucularMethod;

import org.apache.log4j.Logger;

public aspect AspectParticularMethod {

    public static String LOG_MSG = "particularMethod";

    private final static Logger logger = Logger.getLogger(AspectParticularMethod.class);

    pointcut particularMethod() :
            call(void cz.kul.snippets.aspectj.commons.Account.withdraw(int));

    before(): particularMethod() {
        logger.info(LOG_MSG);
    }

}
