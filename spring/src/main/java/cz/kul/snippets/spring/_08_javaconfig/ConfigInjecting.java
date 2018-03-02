package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;
import cz.kul.snippets.spring.common.BeanWithDependency;

@Configuration
public class ConfigInjecting {

	@Bean
	public BeanWithDependency bean2_1(Bean1 bean1) {
		// NOTE: This is a preferred way according to me.
		return new BeanWithDependency(bean1);
	}

	@Bean()
	public BeanWithDependency bean2_2() {
		// NOTE:
		// How does it works? I expected it create new Bean1 instance, but
		// Spring ensure Bean1 is really singleton.
		return new BeanWithDependency(bean1());
	}
	
	@Bean 
	public Bean1 bean1() {
		return new Bean1();
	}
	
}
