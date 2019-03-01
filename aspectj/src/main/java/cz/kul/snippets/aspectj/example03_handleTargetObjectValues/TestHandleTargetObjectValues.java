package cz.kul.snippets.aspectj.example03_handleTargetObjectValues;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.commons.Account;
import cz.kul.snippets.aspectj.commons.AccountHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TestHandleTargetObjectValues extends SnippetsTest {

    private int ORIGINAL_BALANCE = 10;
    private int NEW_BALANCE = 20;

    private Account account;
    private AccountHandler accountHandler;

    @Before
    public void before() {
        account = new Account(ORIGINAL_BALANCE);
        accountHandler = new AccountHandler();
    }

    @Test
    public void thisAndTargetPointcut() {
        accountHandler.handleInsert(account, NEW_BALANCE);
        assertMessageCountInLog(1, AspectThisAndTarget.crateLogMsg(ORIGINAL_BALANCE));
    }

}
