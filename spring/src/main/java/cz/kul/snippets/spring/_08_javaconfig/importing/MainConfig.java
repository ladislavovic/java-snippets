package cz.kul.snippets.spring._08_javaconfig.importing;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(ImportedConfig.class)
@PropertySource(name = "propertyPlaceholderConfigurer", value = "classpath:/cz/kul/snippets/spring/_08_javaconfig/importing/a.properties", ignoreResourceNotFound = true)
public class MainConfig {
	

}
