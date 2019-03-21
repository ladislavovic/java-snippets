package cz.kul.snippets.java.example25_TODO;

import cz.kul.snippets.SnippetsTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;


public class TestTODO extends SnippetsTest {

    private static Logger LOGGER = LoggerFactory.getLogger(TestTODO.class);
    
    @Test
    public void verboseClass() throws InterruptedException {

        Process process = executeProcessAndRedirectOutput(
                "java -cp target/classes -verbose:class cz.kul.snippets.java.example25_TODO.App",
                new String[0],
                null,
                LOGGER);
        process.waitFor();
    }
    
    @Test
    public void classloaders() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        do {
            System.out.println(classLoader.toString() + " class: " + classLoader.getClass().getName());
            
        } while ((classLoader = classLoader.getParent()) != null);
    }
    
    @Test
    public void extDirs() {
        System.out.println(System.getProperty("java.ext.dirs"));
    }
    
    
    /**
     * JMX was introduced in Java 1.5
     */
    @Ignore
    @Test
    public void jmx() throws
            MalformedObjectNameException,
            NotCompliantMBeanException,
            InstanceAlreadyExistsException,
            MBeanRegistrationException,
            InterruptedException {
        PersonFactory personFactory = new PersonFactory();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("cz.kul.snippets.java.example25_TODO:type=PersonFactory");
        server.registerMBean(personFactory, mbeanName);
        
        for (int i = 100; i > 0; i--) {
            personFactory.createPerson(RandomStringUtils.randomAlphabetic(5));
            Thread.sleep(3000);
        }

    }
    
}
