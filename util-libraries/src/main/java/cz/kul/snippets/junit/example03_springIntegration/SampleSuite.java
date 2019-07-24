package cz.kul.snippets.junit.example03_springIntegration;


import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.junit.example02_runTestsProgrammatically.TestRunTestProgrammatically;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SampleTestInjection.class
})
public class SampleSuite {
    
    @BeforeClass
    public static void before() {
        AgentManager.executeAgent(TestRunTestProgrammatically.AGENT, "suite_before");
    }
    
}
