package cz.kul.snippets.aspectj._01_pointcuts.aspects;

import org.apache.log4j.Logger;

public aspect AspectsWildcards {

    final static Logger logger = Logger.getLogger(AspectsWildcards.class);

    pointcut wildcards() :
            call(* cz.kul.snippets.aspectj._01_pointcuts.Account.*(*));

    before(): wildcards() {
        logger.info("wildcards");
    }
}
