package cz.kul.snippets.spring._08_javaconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import cz.kul.snippets.spring._08_javaconfig.scan1.ScannedBean1;
import cz.kul.snippets.spring._08_javaconfig.scan2.ScannedBean2;

/**
 * Notes
 * 
 * These features are not covered by snippets
 * <ul>
 * <li>@Import annotation</li>
 * <li>@Bean(initMethod = "init", destroyMethod = "cleanup" )</li>
 * <li>@Bean @Scope("prototype")</li>
 * <li>@ComponentScan("cz.kul.prime")</li>
 * <li>@Bean can be also in @Component class but it is not preffered</li>
 * <li>Spring allows for using @Bean annotation on methods that are declared in
 * classes not annotated with @Configuration. This is known as “lite” mode. In
 * this mode, bean methods can be declared in a @Component or a plain java class
 * without any annotation. In the “lite” mode, @Bean methods cannot declare
 * inter-bean dependencies. It is recommended that one @Bean method should not
 * invoke another @Bean method in ‘lite’ mode. Spring recommends that @Bean
 * methods declared within @Configuration classes should be used for full
 * configuration. This kind of full mode can prevent many bugs.</li>
 * <li>Remember that @Configuration classes are meta-annotated with @Component, so they
are candidates for component-scanning!</li>
 * <li>@Description annotation can be useful</li>
 * <li>Constructor injection in @Configuration classes is only supported as of Spring Framework
4.3.</li>
 * </ul>
 * 
 * @author kulhanek
 *
 */
public class Main_SpringJavaconfig {

	public static void main(String[] args) {
		configurationAndBean();
		injecting();
		inheritance();
		overridingByInheritance();
		overridingByComposing();		
	}

	private static void overridingByComposing() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config_ConcreteOverrideByComposing.class);
			ctx.refresh();
			Bean1 bean = ctx.getBean(Bean1.class);
			assertEquals("bean was not redefined", "concrete", bean.getValue());
			ScannedBean1 sb1 = ctx.getBean(ScannedBean1.class);
			ScannedBean2 sb2 = ctx.getBean(ScannedBean2.class);
			assertNotNull("Bean scanned by general config was not found", sb1);
			assertNotNull("Bean scanned by concrete config was not found", sb2);
		}
		
	}

	private static void overridingByInheritance() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config_RealOverrideByInheritance.class);
			ctx.refresh();
			ScannedBean1 sb1 = ctx.getBean(ScannedBean1.class);
			ScannedBean2 sb2 = ctx.getBean(ScannedBean2.class);
			assertNotNull(sb1);
			assertNotNull(sb2);
		}
	}

	private static void inheritance() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config_realConfiguration.class);
			ctx.refresh();
			Bean1 foo = (Bean1) ctx.getBean("foo");
			Bean1 bar = (Bean1) ctx.getBean("bar");
			Bean1 baz = (Bean1) ctx.getBean("baz");
			assertEquals("foo", foo.getValue());
			assertEquals("bar_child", bar.getValue());
			assertEquals("baz", baz.getValue());
		}
	}

	private static void injecting() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config_Injecting.class);
			ctx.refresh();
			Bean1 bean1 = ctx.getBean(Bean1.class);
			Bean2 bean2_1 = (Bean2) ctx.getBean("bean2_1");
			Bean2 bean2_2 = (Bean2) ctx.getBean("bean2_2");
			assertTrue(bean1 == bean2_1.getBean1());
			assertTrue(bean1 == bean2_2.getBean1());
		}
	}

	private static void configurationAndBean() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config_ConfigurationAndBean.class); // you can register more classes here
			ctx.refresh();
			Bean1 bean = ctx.getBean(Bean1.class);
			assertNotNull(bean);
		}
	}

}
