package cz.kul.snippets.junit.example02_runTestsProgrammatically;


import cz.kul.snippets.agent.AgentManager;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SampleTest.class
})
public class SampleSuite {
    
    @BeforeClass
    public static void before() {
        AgentManager.executeAgent(TestRunTestProgrammatically.AGENT, "suite_before");
    }
    
}
