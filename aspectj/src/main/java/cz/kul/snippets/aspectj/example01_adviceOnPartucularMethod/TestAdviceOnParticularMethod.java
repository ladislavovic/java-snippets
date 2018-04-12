package cz.kul.snippets.aspectj.example01_adviceOnPartucularMethod;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.commons.Account;
import cz.kul.snippets.aspectj.commons.AccountHandler;
import org.junit.*;

public class TestAdviceOnParticularMethod extends SnippetsTest {

    @Test
    public void test() {
        Account account = new Account();
        account.withdraw(10);
        assertMessageCountInLog(1, AspectParticularMethod.LOG_MSG);
    }



    @Test
    public void thisAndTargetPointcut() {
        Account account = new Account();
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.handleInsert(account, 20);
        assertMessageCountInLog(1, "thisAndTarget");
    }



}

