package cz.kul.snippets.spring._11_parentcontext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(name = "source1", value = "classpath:/config.properties", ignoreResourceNotFound = true)
public class ParentCfg2 {

	// To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
