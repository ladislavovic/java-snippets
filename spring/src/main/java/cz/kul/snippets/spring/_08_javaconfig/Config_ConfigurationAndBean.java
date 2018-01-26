package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config_ConfigurationAndBean {

	@Bean
	public Bean1 fooBean() {
		return new Bean1();
	}
}
