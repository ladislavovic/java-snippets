package cz.kul.snippets.gatewaygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
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
