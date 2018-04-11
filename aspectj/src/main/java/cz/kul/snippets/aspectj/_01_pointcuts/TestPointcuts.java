package cz.kul.snippets.aspectj._01_pointcuts;

import cz.kul.snippets.MemmoryAppenderHelper;
import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj._01_pointcuts.aspects.AspectParticularMethod;
import org.junit.*;

import static org.junit.Assert.assertTrue;


/**
 * TODO comb
 *
 * terms
 *
 * woven
 * poincut
 * aspect
 * join point
 * advice
 *
 * crosscutting concerns - I understand it as problem, which is spread over an application
 *
 * A join point - is a well-defined point in the program flow.
 *
 * A pointcut - picks out certain join points and values at those points.
 *
 * Advice - pointcut plus a code which should be executed when the program reach the pointcut. There is several type
 * of advices: before, after returning, after throwing, after (like finally), around
 *
 * AspectJ's aspect - are the unit of modularity
 *
 * Each method call at runtime is a different join point, even if it comes from the same call expression in the program.
 *
 * what happen when aspect call method which has another aspect assign? Does it works?
 *
 * TODO: I ended before Advice chapter
 *
 */
public class TestPointcuts extends SnippetsTest {

    @Test
    public void particularMethodPointcut() {
        Account account = new Account();
        account.withdraw(10);
        assertMessageCountInLog(1, AspectParticularMethod.LOG_MSG);
    }

    @Test
    public void wildcardsPointcut() {
        Account account = new Account();
        account.withdraw(10);
        account.insert(10);
        assertMessageCountInLog(2, "wildcards");
    }

    @Test
    public void thisAndTargetPointcut() {
        Account account = new Account();
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.handleInsert(account, 20);
        assertMessageCountInLog(1, "thisAndTarget");
    }



}

