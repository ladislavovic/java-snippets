package cz.kul.snippets.junit.example01_beforeclass;

import cz.kul.snippets.agent.AgentManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test2 {
    
    @BeforeClass
    public static void test2BeforeClass() {
        AgentManager.executeAgent(TestBeforeclass.AGENT, "test2_before_class");
    }
    
    @Test
    public void test2_1() {
    }
    
    @Test
    public void test2_2() {
    }
    
}
