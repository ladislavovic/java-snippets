package cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage;

import org.apache.log4j.Logger;

public aspect AspectsAllGettersAndSettersInPackage {

    public static final String LOG_MSG = "allGettersAndSettersInPackage";

    final static Logger logger = Logger.getLogger(AspectsAllGettersAndSettersInPackage.class);

    // NOTE: ".." in package path means any subpackage
    pointcut gettersAndSetters():
                call(* cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage.pkg..*.get*()) ||
                call(boolean cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage.pkg..*.is*()) ||
                call(void cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage.pkg..*.set*(..));

    before(): gettersAndSetters() {
        logger.info(LOG_MSG);
    }

}
