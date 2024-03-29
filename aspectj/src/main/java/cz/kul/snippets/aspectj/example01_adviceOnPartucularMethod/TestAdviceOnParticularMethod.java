package cz.kul.snippets.aspectj.example01_adviceOnPartucularMethod;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.commons.Account;
import org.junit.*;

@Ignore
public class TestAdviceOnParticularMethod extends SnippetsTest {

    @Test
    public void test() {
        Account account = new Account(20);
        account.withdraw(10);
        assertMessageCountInLog(1, ParticularMethodAspect.LOG_MSG);
    }

}

