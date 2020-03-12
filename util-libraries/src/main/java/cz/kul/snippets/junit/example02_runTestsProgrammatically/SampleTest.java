package cz.kul.snippets.junit.example02_runTestsProgrammatically;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleTest {
    
    @Before
    public void before() {
        AgentManager.executeAgent(TestRunTestProgrammatically.AGENT, "test_before");
    }
    
    @Test
    public void test1() {
        AgentManager.executeAgent(TestRunTestProgrammatically.AGENT, "test1");
    }
    
    @Test
    public void test2() {
        AgentManager.executeAgent(TestRunTestProgrammatically.AGENT, "test2");
    }
    
}
