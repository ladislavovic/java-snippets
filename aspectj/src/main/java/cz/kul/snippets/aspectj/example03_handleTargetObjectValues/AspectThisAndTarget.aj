package cz.kul.snippets.aspectj.example03_handleTargetObjectValues;

import cz.kul.snippets.aspectj.commons.Account;
import cz.kul.snippets.aspectj.commons.AccountHandler;
import org.apache.log4j.Logger;

public aspect AspectThisAndTarget {

    final static Logger logger = Logger.getLogger(AspectThisAndTarget.class);

    public static String crateLogMsg(int balance) {
        return "thisAndTarget. Balance: " + balance;
    }

    private Object createLogMsg(int balance) {
        return AspectThisAndTarget.crateLogMsg(balance);
    }

    pointcut thisAndTarget(AccountHandler ah, Account a) :
            this(ah) && target(a);

    before(AccountHandler ah, Account a): thisAndTarget(ah, a) {
        // NOTE: can not call sattic methods of aspect here. This is the reason
        // why createLogMsg method is twice in this aspect
        logger.info(createLogMsg(a.getBalance()));
    }

}
