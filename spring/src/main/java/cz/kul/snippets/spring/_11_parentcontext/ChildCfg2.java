package cz.kul.snippets.spring._11_parentcontext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import cz.kul.snippets.spring.common.Bean1;

@Configuration
public class ChildCfg2 {

	@Value("${value1}")
	String val;

	@Bean
	public Bean1 bean1() {
		Bean1 bean1 = new Bean1(val);
		return bean1;
	}

	// To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
