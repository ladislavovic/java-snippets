package cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.aspectj.commons.Account;
import cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage.pkg.Foo;
import cz.kul.snippets.aspectj.example02_adviceOnAllGettersInPackage.pkg.subpkg.Bar;
import org.junit.Before;
import org.junit.Test;

public class TestAdviceOnAllGettersInPackage extends SnippetsTest {

    private Foo foo;
    private Bar bar;

    @Before
    public void before() {
        foo = new Foo();
        foo.setField1("foo");
        foo.getField1();
        foo.isField3();

        bar = new Bar();
        bar.setField1("bar");
    }

    @Test
    public void test() {
        assertMessageCountInLog(4, AspectsAllGettersAndSettersInPackage.LOG_MSG);
    }
}
