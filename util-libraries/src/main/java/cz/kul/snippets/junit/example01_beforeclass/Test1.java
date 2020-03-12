package cz.kul.snippets.junit.example01_beforeclass;

import cz.kul.snippets.agent.AgentManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Test1 {
    
    @BeforeClass
    public static void test1BeforeClass() {
        AgentManager.executeAgent(TestBeforeclass.AGENT, "test1_before_class");
    }

    @Test
    public void test1_1() {
    }
    
    @Test
    public void test1_2() {
    }
    
}
