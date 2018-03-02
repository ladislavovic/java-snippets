package cz.kul.snippets.spring._06_unittesting;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * When you test Spring, you usually need to create ApplicationContext
 * ang get beans which you want to test.
 * 
 * You can meke it more easy when you use spring test runner and specify
 * in annotation xml configuration, which you want to load for testing.
 * Then Spring autowire object into test and you avoid boilerplate code.
 * 
 * @author kulhalad
 * @since 7.4
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:_06_spring.xml")
public class SpringAppTest {
    
    @Autowired
    protected ApplicationContext ac;

    @Autowired
    private HelloService helloService;

    @Test
    public void testSayHello() {
        Assert.assertEquals("hello", helloService.sayHello());
    }

}
