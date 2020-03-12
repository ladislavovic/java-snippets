package cz.kul.snippets.junit.example03_springIntegration;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class TestSpringIntegration {
    
    @Test
    public void testInjection() {
        JUnitCore junit = new JUnitCore();
        junit.run(SampleTestInjection.class);
    }
    
    @Test
    public void springContextIsCreatedOnceWhenYouHaveMoreTestsInClass() {
        AgentManager.addAgent(SampleTestOnlyOneContext.class.getSimpleName(), x -> x);
        JUnitCore junit = new JUnitCore();
        junit.run(SampleTestOnlyOneContext.class);
        List<Object> contexts = AgentManager.getAgentLog(SampleTestOnlyOneContext.class.getSimpleName()).getResults();
        ApplicationContext ctx1 = (ApplicationContext) contexts.get(0);
        ApplicationContext ctx2 = (ApplicationContext) contexts.get(1);
        Assert.assertNotNull(ctx1);
        Assert.assertNotNull(ctx2);
        Assert.assertTrue(ctx1 == ctx2);
    }
    
    
}
