package cz.kul.snippets.springdoc.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
this.getClass(),
org.springdoc.ui.SwaggerConfig.class,
org.springdoc.core.SwaggerUiConfigProperties.class,
org.springdoc.core.SwaggerUiOAuthProperties.class,
org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
org.springdoc.webmvc.core.MultipleOpenApiSupportConfiguration.class,
org.springdoc.core.SpringDocConfiguration.class,
org.springdoc.core.SpringDocConfigProperties.class,
org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
 */


@Configuration

@Import({
//		org.springdoc.webmvc.ui.SwaggerConfig.class,
//		org.springdoc.core.SwaggerUiConfigProperties.class,
//		org.springdoc.core.SwaggerUiOAuthProperties.class,
//		org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
//		org.springdoc.webmvc.core.MultipleOpenApiSupportConfiguration.class,
//		org.springdoc.core.SpringDocConfiguration.class,
//		org.springdoc.core.SpringDocConfigProperties.class,
//		org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
})

@ComponentScan({
		"cz.kul.snippets.springdoc.app",
		"rest",
//		"org.springdoc.ui",
		"org.springdoc.core",
		"org.springdoc.webmvc.core",
//		"org.springdoc.webmvc.ui",
//		"org.springframework.boot.autoconfigure.jackson"
})

@PropertySource("classpath:springdoc.properties")
@EnableWebMvc
public class ServletContextConfiguration implements WebMvcConfigurer {

//	@Bean
//	public GroupedOpenApi publicApi() {
//		return GroupedOpenApi.builder()
//				.group("springshop-public")
//				.pathsToMatch("/public/**")
//				.build();
//	}

}
