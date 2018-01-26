package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("cz.kul.snippets.spring._08_javaconfig.scan1")
public class Config_GeneralOverrideByComposing {
	
	@Bean
	public Bean1 foo() {
		Bean1 bean = new Bean1();
		bean.setValue("general");
		return bean;
	}

}
