package cz.kul.snippets.gatewaygateway;

import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class GatewayApplication {

	@Autowired
	RouteDefinitionLocator locator;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

//	@Bean
//	public GroupedOpenApi g1() {
//		return GroupedOpenApi.builder().pathsToMatch("/api1/**").group("api1").build();
//	}
//
//	@Bean
//	public GroupedOpenApi g2() {
//		return GroupedOpenApi.builder().pathsToMatch("/api2/**").group("api2").build();
//	}

	@Bean
	@Primary
	public SwaggerUiConfigProperties configProperties() {

		AbstractSwaggerUiConfigProperties.SwaggerUrl url1 = new AbstractSwaggerUiConfigProperties.SwaggerUrl("api1", "/api1/v3/api-docs", "api1");
		AbstractSwaggerUiConfigProperties.SwaggerUrl url2 = new AbstractSwaggerUiConfigProperties.SwaggerUrl("api2", "/api2/v3/api-docs", "api2");

		SwaggerUiConfigProperties props = new SwaggerUiConfigProperties();
		props.setUrls(Set.of(url1, url2));
		return props;
	}

	//	@Bean
	public List<GroupedOpenApi> apis() {
//		List<GroupedOpenApi> groups = new ArrayList<>();
//		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//		if (definitions != null) {
//			for (RouteDefinition definition : definitions) {
//				String name = definition.getId();
//				GroupedOpenApi group = GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
//				groups.add(group);
//			}
//		}
//		return groups;
		return List.of(
				GroupedOpenApi.builder().pathsToMatch("/api1/**").group("api1").build(),
				GroupedOpenApi.builder().pathsToMatch("/api2/**").group("api2").build());
	}

//	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {

//		return builder
//				.routes()
//				.route(p -> p
//						.path("/get")
//						.filters(f -> f.addRequestHeader("Hello", "World"))
//						.uri("http://httpbin.org:80"))
//				.build();


//		return builder
//				.routes()
//				.route("toApi1_route", r -> r
//						.path("/api1/**")
//						.filters(f -> f.rewritePath("/api1/?(?<segment>.*)", "/${segment}"))
//						.uri("http://localhost:8889"))
//				.build();

		return null;

	}

}
