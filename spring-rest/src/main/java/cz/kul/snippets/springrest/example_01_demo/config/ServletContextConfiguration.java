package cz.kul.snippets.springrest.example_01_demo.config;

import cz.kul.snippets.springrest.example_01_demo.PackageIdentifier;
import cz.kul.snippets.springrest.example_01_demo.SpringInterceptor;
import cz.kul.snippets.springrest.example_01_demo.messageconvertor.LoggingJsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;
import java.util.Map;

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

	@Autowired
	private ApplicationContext ctx;

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

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SpringInterceptor());
//		registry.addWebRequestInterceptor(new SpringWebRequestInterceptor());
	}

//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		converters.add(0, new CustomMessageConverter());
//	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(0, new LoggingJsonMessageConverter());
	}

	@EventListener(ContextRefreshedEvent.class)
	public void registerInterceptor() throws ReflectiveOperationException {
		Map<String, RequestMappingHandlerAdapter> beansOfType = ctx.getBeansOfType(RequestMappingHandlerAdapter.class);
//		HandlerMethodArgumentResolverComposite bean = ctx.getBean(HandlerMethodArgumentResolverComposite.class);
		System.out.println(beansOfType);
		if (!beansOfType.isEmpty()) {
			RequestMappingHandlerAdapter handler = beansOfType.values().iterator().next();
			for (HandlerMethodArgumentResolver argumentResolver : handler.getArgumentResolvers()) {
				if (argumentResolver instanceof RequestResponseBodyMethodProcessor) {
					RequestResponseBodyMethodProcessor retyped = (RequestResponseBodyMethodProcessor) argumentResolver;
					System.out.println(retyped);
				}
			}
		}


//		System.out.println(bean);
	}

	//	@PostConstruct
//	public void registerInterceptor() {
//		System.out.println("aaa");
////		InterceptorRegistry registry = CrossApiContextProvider.getBean(RequestMappingHandlerMapping.class);
//		Map<String, RequestMappingHandlerMapping> beans = ctx.getBeansOfType(RequestMappingHandlerMapping.class);
//		System.out.println(beans);
//	}

}
