package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class Config03_realConfiguration extends Config03_abstractConfiguration {

	@Override
	@Bean
	public Bean1 bar() {
		Bean1 bean1 = new Bean1();
		bean1.setVal("bar_child");
		return bean1;
	}
	
	@Override
	@Bean
	public Bean1 baz() {
		Bean1 bean1 = new Bean1();
		bean1.setVal("baz_child");
		return bean1;
	}
	
}

