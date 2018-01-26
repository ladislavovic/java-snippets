package cz.kul.snippets.spring._08_javaconfig.importing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class ImportedConfig {

	@Value("${value1}")
	private String value1;

	@Bean
	public Bean1 bean1() {
		Bean1 bean1 = new Bean1(value1);
		return bean1;
	}

	// To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
