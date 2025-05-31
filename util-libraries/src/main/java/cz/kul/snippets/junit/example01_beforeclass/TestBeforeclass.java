package cz.kul.snippets.junit.example01_beforeclass;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestBeforeclass {

    public static String AGENT = TestBeforeclass.class.getSimpleName() + "_agent";

    @Before
    public void setAgent() {
        AgentManager.addAgent(AGENT, x -> x);
    }

    @Test
    public void suiteBC_is_before_testBC() {
        JUnitCore junit = new JUnitCore();
        junit.run(SuiteBeforeClass.class);

        Object[] agentLog = AgentManager.getAgentLog(AGENT).getResults().toArray();
        Assert.assertArrayEquals(
                new Object[] {"suite_before_class", "test1_before_class", "test2_before_class", "suite_after_class"},
                agentLog);
    }

}
