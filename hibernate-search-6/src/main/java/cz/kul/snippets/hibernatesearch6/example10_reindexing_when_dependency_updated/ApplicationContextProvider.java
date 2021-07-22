package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}
	public static <T> T getBean(Class<T> requiredType, String name) {
		return context.getBean(name,requiredType);
	}

}
