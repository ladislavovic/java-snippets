package cz.kul.snippets.aspectj.example04_exceptionTranslation;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.example04_exceptionTranslation.pkg.Calculator;
import org.junit.Test;

public class TestExceptionTranslation extends SnippetsTest {

    @Test(expected = MyException.class)
    public void thisAndTargetPointcut() {
        Calculator calculator = new Calculator();
        calculator.abs(null); // it throws NPE
    }

}
