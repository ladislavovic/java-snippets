package cz.kul.snippets.spring.common;

import java.util.function.Consumer;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringTestUtils {

	public static void runInSpring(Class<?> config, Consumer<ApplicationContext> consumer) {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(config);
			ctx.refresh();
			consumer.accept(ctx);
		}
	}

	// TODO not only spring issue
	public static MemmoryAppender getMemmoryAppender() {
		String DEFAULT_APPENDER_NAME = "MEMMORY_APPENDER";
		return getMemmoryAppender(DEFAULT_APPENDER_NAME);
    }

    // TODO not only spring issue
	public static MemmoryAppender getMemmoryAppender(String appenderName) {
		MemmoryAppender a = (MemmoryAppender) Logger.getRootLogger().getAppender(appenderName);
		return a;
	}

	// TODO not only spring issue
	public static void cleanMemmoryAppender() {
		String DEFAULT_APPENDER_NAME = "MEMMORY_APPENDER";
		cleanMemmoryAppender(DEFAULT_APPENDER_NAME);
	}

	// TODO not only spring issue
	public static void cleanMemmoryAppender(String appenderName) {
		MemmoryAppender a = (MemmoryAppender) Logger.getRootLogger().getAppender(appenderName);
		a.clean();
	}

	/**
	 * It returns how many times were beans registered in the context. It takes datea from logging.
	 *
	 * For example when it register "bean1" and then overrides this bean by another registration
	 * (so again "bean1") it returns 2. Only one bean is registered but registration occured two
	 * times.
	 */
	public static int getBeanRegistrationCount() {
		return getMemmoryAppender().findEventsCount("Registering bean definition");
	}

	/**
	 * True if bean overriding according to given parameter is in log.
	 *
	 * It expects log message like this:
	 * 2018-03-14 14:03:07 DefaultListableBeanFactory [INFO] Overriding bean definition for bean 'bean1' with
	 * a different definition: replacing [Root bean: class [null]; scope=; abstract=false; lazyInit=false;
	 * autowireMode=3; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=AParent;
	 * factoryMethodName=bean1; initMethodName=null; destroyMethodName=(inferred); defined in class path resource
	 * [cz/kul/snippets/spring/_12_javaconfig_configuration_inheritance/module1/AParent.class]]
	 * with [Root bean: class [null]; scope=; abstract=false; lazyInit=false; autowireMode=3; dependencyCheck=0;
	 * autowireCandidate=true; primary=false; factoryBeanName=child; factoryMethodName=bean1; initMethodName=null;
	 * destroyMethodName=(inferred); defined in class path resource
	 * [cz/kul/snippets/spring/_12_javaconfig_configuration_inheritance/module1/Child.class]]
	 *
	 */
	public static boolean isBeanOverriding(String beanName, Class<?> oldConfigClass, Class<?> newConfigClass) {
		StringBuilder pattern = new StringBuilder();
		pattern.append("Overriding bean definition for bean '" + beanName + "' with .*");
		pattern.append("replacing.*");
		pattern.append("defined in.*");
		pattern.append(oldConfigClass.getSimpleName() + ".*");
		pattern.append("with.*");
		pattern.append("defined in.*");
		pattern.append(newConfigClass.getSimpleName() + ".*");
		int eventsCount = getMemmoryAppender().findEventsCount(pattern.toString());
		if (eventsCount > 1) {
			throw new IllegalStateException("It is weird, probably there is something wrong with log parsing");
		}
		return eventsCount == 1;
	}

}
