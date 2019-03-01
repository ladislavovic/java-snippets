package cz.kul.snippets.spring._09_javaconfig_and_overriding;

import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule1.RootConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule10.RootInner2ConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule2.ParentConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule3.Importee1ConfigByComponentSccan2;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule4.Importee2ConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule5.Importee1ConfigByComponentSccan1;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule6.RootInner1ConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule7.ParentInnerConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule8.Importee1InnerConfigByComponentScan;
import cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule9.ParentOfRootInner1ConfigByComponentScan;
import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static cz.kul.snippets.spring.common.SpringTestUtils.isBeanOverriding;
import static org.junit.Assert.assertEquals;

public class TestOverriding {

    @Before
    public void beforeEachTest() {
        SpringTestUtils.cleanMemmoryAppender();
    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule3")
    public static class Importee1 {

        @Bean
        Bean1 bean1() {
            return new Bean1("importee1");
        }

        @Configuration
        @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule8")
        public static class Importee1Inner {

            @Bean(name = "bean1")
            Bean1 bean1() {
                return new Bean1("importee1Inner");
            }

        }

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule4")
    public static class Importee2 {

        @Bean
        Bean1 bean1() {
            return new Bean1("importee2");
        }

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule2")
    public static class Parent {

        @Bean
        Bean1 bean1() {
            return new Bean1("ancestor");
        }

        @Configuration
        @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule7")
        public static class ParentInner {

            @Bean(name = "bean1")
            Bean1 bean1() {
                return new Bean1("ancestorInner");
            }

        }

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule9")
    public static class ParentOfInner {

        @Bean
        Bean1 bean1() {
            return new Bean1("parentOfInner");
        }

    }

    @Configuration
    @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule1")
    @Import({
            Importee1.class,
            Importee2.class
    })
    public static class Root extends Parent {

        @Bean(name = "bean1")
        Bean1 bean1() {
            return new Bean1("child");
        }

        @Configuration
        @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule6")
        public static class RootInner1 extends ParentOfInner {

            @Bean(name = "bean1")
            @Override
            Bean1 bean1() {
                return new Bean1("childInner1");
            }

        }

        @Configuration
        @ComponentScan("cz.kul.snippets.spring._09_javaconfig_and_overriding.componentScanModule10")
        public static class RootInner2 {

            @Bean(name = "bean1")
            Bean1 bean1() {
                return new Bean1("childInner2");
            }

        }

    }

    /**
     * Spring create bean definitions this way:
     * <h3>Phase 1: Do Component Scan for definitions on:</h3>
     * <ol>
     *     <li>inner classes</li>
     *     <li>the class</li>
     *     <li>imported classes</li>
     *     <li>parent class</li>
     * </ol>
     *
     * <p>Note 1: order of inner classes is according to position in the class. The last
     * inner class is scanned as firs</p>
     *
     * <p>Note 2: it is recursive. If for example imported class has a inner class or parent class
     * or import another class it is also processed in the same order</p>
     *
     * <p>Note 3: if another configuration class is foundd during component scan, it is also
     * immediately recursivelly processed</p>
     *
     * <h3>Phase 2: Create Bean definitions from: </h3>
     * <ol>
     *     <li>Inner classes</li>
     *     <li>Configurations found during component scan defined on the class (not defined on parent, import, ...)</li>
     *     <li>Imported classes</li>
     *     <li>Configurations found during component scan defined on the parent</li>
     *     <li>The class</li>
     * </ol>
     *
     * <p>Note 1: It is again recursive process. If for example imported class has a component scan
     * defined which found another configuration, first BD for the found configuration is created
     * and then for the imported class itself.</p>
     *
     * <p>Note2: Generally child can override definitions from parent and from imported class. This
     * is expected and needed behaviour. But there is a problem with component scan. First are
     * created BD from configurations found by component scan defined directly on the
     * class and later on other BDs. So BD from component scan are overriden for example by import
     * or ancestor. I do not think it is useful order.
     */
    @Test
    @Ignore // logging parsing was not working
    public void orderTest() {
        SpringTestUtils.runInSpring(TestOverriding.Root.class, ctx -> {
            Bean1 bean = (Bean1) ctx.getBean("bean1");

            assertEquals("child", bean.getVal());
            assertEquals(17, SpringTestUtils.getBeanRegistrationCount());
            assertBeanOverridingChain("bean1",
                    RootInner2ConfigByComponentScan.class,
                    Root.RootInner2.class,
                    RootInner1ConfigByComponentScan.class,
                    ParentOfRootInner1ConfigByComponentScan.class,
                    Root.RootInner1.class,
                    RootConfigByComponentScan.class,
                    Importee1InnerConfigByComponentScan.class,
                    Importee1.Importee1Inner.class,
                    Importee1ConfigByComponentSccan1.class,
                    Importee1ConfigByComponentSccan2.class,
                    Importee1.class,
                    Importee2ConfigByComponentScan.class,
                    Importee2.class,
                    ParentInnerConfigByComponentScan.class,
                    Parent.ParentInner.class,
                    ParentConfigByComponentScan.class,
                    Root.class
            );

        });
    }

    private void assertBeanOverridingChain(String beanName, Class<?>... classes) {
        for (int i = 0; i < classes.length - 1; i++) {
            if (!isBeanOverriding(beanName, classes[i], classes[i + 1])) {
                throw new AssertionError(String.format("There is no bean overriding from %s to %s", classes[i], classes[i + 1]));
            }
        }
    }

}
