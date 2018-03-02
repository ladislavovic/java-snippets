package cz.kul.snippets.spring._08_javaconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.BeanWithDependency;

/**
 * TODO:
 *  - lite mode
 * 
 */
public class Test_SpringJavaconfig {

	@Test
	public void testHelloWorld() {
		SpringTestUtils.runInSpring(ConfigHelloWorld.class, ctx -> {
			Bean1 bean = ctx.getBean(Bean1.class);
			assertNotNull(bean);
		});
	}

	@Test
	public void injecting() {
		SpringTestUtils.runInSpring(ConfigInjecting.class, ctx -> {
			Bean1 bean1 = ctx.getBean(Bean1.class);
			BeanWithDependency bean2_1 = (BeanWithDependency) ctx.getBean("bean2_1");
			BeanWithDependency bean2_2 = (BeanWithDependency) ctx.getBean("bean2_2");
			assertTrue(bean1 == bean2_1.getBean1());
			assertTrue(bean1 == bean2_2.getBean1());
		});
	}

	private static void configurationInheritance() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config03_realConfiguration.class);
			ctx.refresh();
			Bean1 foo = (Bean1) ctx.getBean("foo");
			Bean1 bar = (Bean1) ctx.getBean("bar");
			Bean1 baz = (Bean1) ctx.getBean("baz");
			assertEquals("foo", foo.getVal());
			assertEquals("bar_child", bar.getVal());
			assertEquals("baz_child", baz.getVal());
		}
	}

	private static void collectionAsSpringBean() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(Config04_collectionAsSpringBean.class);
			ctx.refresh();

			// NOTE: this works like any other bean type
			List<Bean1> foo = (List<Bean1>) ctx.getBean("foo");
			
			// NOTE: this really does not work, there is not
			// bean of type Bean1
			try {
				 Bean1 bar = ctx.getBean(Bean1.class);
				 fail("It should not find this bean");
			} catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) { 
			}
		}
		
	}
	
}
