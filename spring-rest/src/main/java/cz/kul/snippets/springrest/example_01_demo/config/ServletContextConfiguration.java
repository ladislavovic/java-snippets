package cz.kul.snippets.springrest.example_01_demo.config;

import cz.kul.snippets.springrest.example_01_demo.PackageIdentifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackageClasses = PackageIdentifier.class)

/*
  * since 3.1
  * it imports MVC configuration from WebMvcConfigurationSupport. This class add many
    beans to context which are needed for MVC.
  * you can also omit this annotation and extends your configuration directly from
    WebMvcConfigurationSupport. It is a good choice if you want modify default configuration
    a lot.
 */
@EnableWebMvc
@Import(SpringfoxContextConfiguration.class)
public class ServletContextConfiguration implements WebMvcConfigurer {

	/*
	The following configuration maps some urls to classpath resource.
	When you use Spring Boot you does not have to do it, it configure
	that automatically.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry
				.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
