package cz.kul.snippets.spring._16_context_refresh_order;

import cz.kul.snippets.agent.AgentLog;
import cz.kul.snippets.agent.AgentManager;
import cz.kul.snippets.spring.common.Bean1;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

public class TestContextRefreshOrder {
    
    private static final String AGENT = TestContextRefreshOrder.class.getSimpleName() + "_AGENT";

    @Configuration
    public static class Cfg {

        @Bean
        public Bean1 bean() {
            AgentManager.executeAgent(AGENT, 1);
            return new Bean1();
        }
        
    }
    
    @Test
    public void beanFactoryMethodIsCalledImmediatellyAfterRefresh() {
        AgentManager.addAgent(AGENT, x -> x);
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
            ctx.register(Cfg.class);
            ctx.refresh();
            AgentManager.executeAgent(AGENT, 2);
            ctx.getBean(Bean1.class);

            AgentLog agentLog = AgentManager.getAgentLog(AGENT);
            Assert.assertArrayEquals(new Object[] {1, 2}, agentLog.getResults().toArray());
        }
    }


}
