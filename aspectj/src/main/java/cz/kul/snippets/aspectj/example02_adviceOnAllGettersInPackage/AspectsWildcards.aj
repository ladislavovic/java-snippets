package cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage;

import org.apache.log4j.Logger;

public aspect AspectsWildcards {

    final static Logger logger = Logger.getLogger(AspectsWildcards.class);

    pointcut wildcards() :
            call(* cz.kul.snippets.aspectj.commons.Account.*(*));

    before(): wildcards() {
        logger.info("wildcards");
    }
}
