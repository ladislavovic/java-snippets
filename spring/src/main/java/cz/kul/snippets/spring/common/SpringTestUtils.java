package cz.kul.snippets.spring.common;

import java.util.function.Consumer;

import cz.kul.snippets.spring._08_javaconfig.Config01_HelloWorld;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.assertNotNull;

public class SpringTestUtils {

	public static void runInSpring(Class<?> config, Consumer<ApplicationContext> consumer) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(config);
			ctx.refresh();
			consumer.accept(ctx);
		}
	}

}
