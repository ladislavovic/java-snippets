package cz.kul.snippets.junit.example01_beforeclass;

import cz.kul.snippets.agent.AgentLog;
import cz.kul.snippets.agent.AgentManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.List;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    Test1.class,
    Test2.class    
})
public class SuiteBeforeClass {
    
    @BeforeClass
    public static void suiteBeforeClass() {
        AgentManager.executeAgent(TestBeforeclass.AGENT, "suite_before_class");
    }
    
    @AfterClass
    public static void suiteAfterClass() {
        AgentManager.executeAgent(TestBeforeclass.AGENT, "suite_after_class");
    }
    
}

