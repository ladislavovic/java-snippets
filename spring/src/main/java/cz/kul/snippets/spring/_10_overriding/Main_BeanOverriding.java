package cz.kul.snippets.spring._10_overriding;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.spring.common.Bean1;

public class Main_BeanOverriding {

	public static void main(String[] args) {
		// beanOverriding();
		test();

	}

	private static void test() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(TestConfig.class);
			ctx.refresh();

			Bean1 byName = (Bean1) ctx.getBean("foo");
			System.out.println("By name: " + byName.getVal());

//			Bean1 byType = ctx.getBean(Bean1.class);
//			System.out.println("By type: " + byType.getVal());

			Map<String, Bean1> beansOfType = ctx.getBeansOfType(Bean1.class);
			beansOfType.entrySet().forEach(x -> System.out.println(x.getKey() + ": " + x.getValue()));
		}

	}

	private static void beanOverriding() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(ContextConfiguration.class);
			ctx.refresh();
			String bean = (String) ctx.getBean("bean");
			assertEquals("overriden", bean);
		}

	}

}
