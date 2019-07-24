package cz.kul.snippets.junit.example03_springIntegration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Cfg.class})
public class SampleTestInjection {
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    private String bean;

    @Test
    public void test1() {
        Assert.assertNotNull(ctx);
        Assert.assertNotNull(bean);
    }
    
}
