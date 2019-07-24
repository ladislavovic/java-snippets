package cz.kul.snippets.junit.example03_springIntegration;

import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.junit.example02_runTestsProgrammatically.TestRunTestProgrammatically;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Cfg.class})
public class SampleTestOnlyOneContext {
    
    @Autowired
    private ApplicationContext ctx;
    
    @Test
    public void test1() {
        AgentManager.executeAgent(SampleTestOnlyOneContext.class.getSimpleName(), ctx);
    }
    
    @Test
    public void test2() {
        AgentManager.executeAgent(SampleTestOnlyOneContext.class.getSimpleName(), ctx);
    }
    
}
