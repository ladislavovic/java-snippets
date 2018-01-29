package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class Config01_HelloWorld {

	@Bean
	public Bean1 fooBean() {
		return new Bean1();
	}
}
