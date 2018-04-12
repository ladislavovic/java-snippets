package cz.kul.snippets.aspectj.example03_handleThisAndTargetInAdvice;

import cz.kul.snippets.aspectj.commons.Account;
import cz.kul.snippets.aspectj.commons.AccountHandler;
import org.apache.log4j.Logger;

public aspect AspectThisAndTarget {

    final static Logger logger = Logger.getLogger(AspectThisAndTarget.class);

    pointcut thisAndTarget(AccountHandler ah, Account a) :
            this(ah) && target(a);

    before(AccountHandler ah, Account a): thisAndTarget(ah, a) {
        logger.info("thisAndTarget");
    }
}
