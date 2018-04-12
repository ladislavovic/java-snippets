package cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.commons.Account;
import org.junit.Test;

public class TestAdviceOnAllGettersInPackage extends SnippetsTest {

    @Test
    public void wildcardsPointcut() {
        Account account = new Account();
        account.withdraw(10);
        account.insert(10);
        assertMessageCountInLog(2, "wildcards");
    }
}
