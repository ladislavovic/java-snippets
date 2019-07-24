package cz.kul.snippets.junit.example02_runTestsProgrammatically;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunTestProgrammatically {
    
    public static String AGENT = TestRunTestProgrammatically.class.getSimpleName() + "_agent";
    
    @Before
    public void setAgent() {
        AgentManager.addAgent(AGENT, x -> x);
    }
    
    @Test
    public void runTestProgrammatically() {
        JUnitCore junit = new JUnitCore();
        junit.run(SampleTest.class);

        Object[] agentLog = AgentManager.getAgentLog(AGENT).getResults().toArray();
        Assert.assertArrayEquals(new Object[] {"test_before", "test1", "test_before", "test2"}, agentLog);
    }
    
    @Test
    public void runSuiteProgrammatically() {
        JUnitCore junit = new JUnitCore();
        junit.run(SampleSuite.class);

        Object[] agentLog = AgentManager.getAgentLog(AGENT).getResults().toArray();
        Assert.assertArrayEquals(
                new Object[] {"suite_before", "test_before", "test1", "test_before", "test2"},
                agentLog
        );
    }
    
    @Test
    public void getResults() {
        JUnitCore junit = new JUnitCore();
        
        Result result = junit.run(SampleTestFailureNPE.class);
        Assert.assertEquals(false, result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getFailures().size());
        Assert.assertEquals(NullPointerException.class, result.getFailures().get(0).getException().getClass());

        result = junit.run(SampleTestFailureAssertion.class);
        Assert.assertEquals(false, result.wasSuccessful());
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getFailures().size());
        Assert.assertEquals(AssertionError.class, result.getFailures().get(0).getException().getClass());
    }
    
}
