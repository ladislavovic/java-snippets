package cz.kul.snippets.spring._08_javaconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config_Injecting {

	@Bean
	public Bean2 bean2_1(Bean1 bean1) {
		// NOTE: This is a preferred way according to me.
		return new Bean2(bean1);
	}

	@Bean()
	public Bean2 bean2_2() {
		// NOTE:
		// How does it works? I expected it create new Bean1 instance, but
		// Spring ensure Bean1 is really singleton.
		return new Bean2(bean1());
	}
	
	@Bean 
	public Bean1 bean1() {
		return new Bean1();
	}
	
}
