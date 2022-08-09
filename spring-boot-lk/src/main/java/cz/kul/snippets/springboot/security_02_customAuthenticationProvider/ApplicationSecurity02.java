package cz.kul.snippets.springboot.security_02_customAuthenticationProvider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationSecurity02 {

	/*

	Wehen you create just one (does not work when you have more) AuthenticationProvider bean,
	it is used for authentication. No additional configuration is needed.

	*/
	public static void main(String[] args) {
		SpringApplication.run(ApplicationSecurity02.class, "--spring.config.name=security_02");
	}

	@Bean
	public CustomAuthenticationProvider prov1() {
		return new CustomAuthenticationProvider("admin");
	}

}
