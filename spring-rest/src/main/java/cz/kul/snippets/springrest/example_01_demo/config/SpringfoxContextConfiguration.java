package cz.kul.snippets.springrest.example_01_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringfoxContextConfiguration {

	public static final String TAG1 = "TAG1";
	public static final String TAG2 = "TAG2";

	/*
	   Configure how the api specification is generated:
	    * It can select which functions will be included in the API.
	    * set API metadata

	   You can have several Doclets here, each for API group.
	 */
	@Bean
	public Docket docketG1() {
		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("g1")
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/person/**")) // here you have path relative to dispatcher servlet mapping
				.build()
				.tags(
						new Tag(TAG1, "The description of tag 1"),
						new Tag(TAG2, "The description of tag 2"))
				.apiInfo(apiInfo());
	}

//	@Bean
//	public Docket docketG2() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.groupName("g2")
//				.select()
//				.apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.ant("/configuration/**"))
//				.build()
//				.apiInfo(apiInfo2());
//	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"API title",
				"The description of the API.",
				"Version of the API",
				"Terms of service???",
				new Contact("Contact name", "www.url_to_contact.com", "contact_email@company.com"),
				"License of API",
				"API license URL",
				Collections.emptyList());
	}

	private ApiInfo apiInfo2() {
		return new ApiInfo(
				"API title 2",
				"The description of the API.",
				"Version of the API",
				"Terms of service???",
				new Contact("Contact name", "www.url_to_contact.com", "contact_email@company.com"),
				"License of API",
				"API license URL",
				Collections.emptyList());
	}

}
