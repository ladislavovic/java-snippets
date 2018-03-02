package cz.kul.snippets.spring._10_overriding;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.kul.snippets.spring.common.Bean1;

public class Main_BeanOverriding {

	public static void main(String[] args) {
		// beanOverriding();
//		test();
		importWin();

	}

	private static void importWin() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(ImportWinConfig.class);
			ctx.refresh();

			Bean1 byName = (Bean1) ctx.getBean("foo");
			assertEquals("config_imported", byName.getVal());
		}
	}

	private static void test() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(TestConfig.class);
			ctx.refresh();

			Bean1 byName = (Bean1) ctx.getBean("foo");
			assertEquals("project", byName.getVal());
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
