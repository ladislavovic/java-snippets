package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config_realConfiguration extends Config_abstractConfiguration {

	@Override
	@Bean
	public Bean1 bar() {
		Bean1 bean1 = new Bean1();
		bean1.setValue("bar_child");
		return bean1;
	}
	
	@Override
	@Bean
	public Bean1 baz() {
		Bean1 bean1 = new Bean1();
		bean1.setValue("baz");
		return bean1;
	}
	
}

