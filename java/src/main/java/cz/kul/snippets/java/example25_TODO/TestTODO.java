package cz.kul.snippets.java.example25_TODO;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.java.example25_TODO.mbean.PersonFactory;
import cz.kul.snippets.java.example25_TODO.mbeanwithcomplextype.Bean1;
import cz.kul.snippets.java.example25_TODO.mbeanwithcomplextype.Bean1ComplexValue;
import cz.kul.snippets.java.example25_TODO.mxbeanwithcomplextype.Bean2Impl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


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
        System.out.println("NOTE: The root class loader is not visible here because it is naive component, does not have Java implementation.");
    }
    
    @Test
    @Ignore("It manipulates with current thread etc., I want to run it explicitelly only.")
    public void classLoaderOfClass() throws Exception {
//        ClassLoader customClassLoader = new CustomClassLoader(this.getClass().getClassLoader());
        ClassLoader customClassLoader = new JarClassLoader(this.getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(customClassLoader);
        
        Class<?> classA = Class.forName("cz.kul.snippets.java.example25_TODO.A", true, customClassLoader);
        Object a = classA.newInstance();

        Field bField = classA.getDeclaredField("b");
        B bObject = (B) bField.get(a);

        System.out.println(classA.getClassLoader());
        System.out.println(bObject.getClass().getClassLoader());
    }
    
    @Test
    public void extDirs() {
        System.out.println(System.getProperty("java.ext.dirs"));
    }
    
    @Test
    public void mBean() throws Exception {
        PersonFactory personFactory = new PersonFactory();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("cz.kul.snippets.java:name=PersonFactory");
        
        server.registerMBean(personFactory, mbeanName);
        Assert.assertEquals(0, server.getAttribute(mbeanName, "NumberOfPersons"));

        for (int i = 100; i > 0; i--) {
            personFactory.createPerson(RandomStringUtils.randomAlphabetic(5));
        }
        Assert.assertEquals(100, server.getAttribute(mbeanName, "NumberOfPersons"));
    }
    
    @Test
    public void mxBeanWithComplexValue() throws Exception {
        Bean1 bean1 = new Bean1();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("cz.kul.snippets.java:name=Bean1");

        server.registerMBean(bean1, mbeanName);
        Bean1ComplexValue value = (Bean1ComplexValue) server.getAttribute(mbeanName, "Value");
        Assert.assertEquals("foo", value.getVal1());
    }
    
    @Test
    public void mxBean() throws Exception {
        Bean2Impl bean2 = new Bean2Impl();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = new ObjectName("cz.kul.snippets.java:name=Bean2");

        server.registerMBean(bean2, mbeanName);
        CompositeData data = (CompositeData) server.getAttribute(mbeanName, "Value");
        Assert.assertEquals("foo", data.get("val1"));
    }
    
    @Test
    public void standardJVMMXBeans() throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        {
            ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean();
            LOGGER.info("### " + bean.getClass().getSimpleName());
            LOGGER.info("LoadedClassCount: " + bean.getLoadedClassCount());
            LOGGER.info("");
        }
        {
            MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
            LOGGER.info("### " + bean.getClass().getSimpleName());
            LOGGER.info("HeapMemoryUsage: " + bean.getHeapMemoryUsage());
            LOGGER.info("");
        }
        {
            OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
            LOGGER.info("### " + bean.getClass().getSimpleName());
            LOGGER.info("Name: " + bean.getName());
            LOGGER.info("Arch: " + bean.getArch());
            LOGGER.info("Version: " + bean.getVersion());
            LOGGER.info("AvailableProcessors: " + bean.getAvailableProcessors());
            LOGGER.info("SystemLoadAverage: " + bean.getSystemLoadAverage());
            LOGGER.info("");
        }
        {
            RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
            LOGGER.info("### " + bean.getClass().getSimpleName());
            LOGGER.info("BootClassPath: " + bean.getBootClassPath());
            LOGGER.info("ClassPath: " + bean.getClassPath());
            LOGGER.info("");
        }
        {
            ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            LOGGER.info("### " + bean.getClass().getSimpleName());
            LOGGER.info("CurrentThreadCpuTime: " + bean.getCurrentThreadCpuTime());
            LOGGER.info("");
        }
    }
    
    @Test
    public void heapDump() throws InterruptedException {
        int num = 10_000_000;
        List<String> strings = new ArrayList<>(num);
        List<Date> dates = new ArrayList<>(num);
        List<Person> people = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            strings.add(RandomStringUtils.random(10));
            dates.add(new Date());
            people.add(new Person(
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    new Date(),
                    1
            ));
        }
        
        int i = 0;
        long lastStart = System.currentTimeMillis();
        while (true) {
            long diff = System.currentTimeMillis() - lastStart;
            if (diff > 200) {
                System.out.println("The big diff: " + diff);
            }
            lastStart = System.currentTimeMillis();
            System.out.println(i++);
            if (i > 9) {
                i = 0;
            }
            Thread.sleep(100);
        }
        
        
    }
    
    public static class Person {
        String name, surname;
        Date birth;
        int sex;

        public Person(String name, String surname, Date birth, int sex) {
            this.name = name;
            this.surname = surname;
            this.birth = birth;
            this.sex = sex;
        }
    }
    
}
