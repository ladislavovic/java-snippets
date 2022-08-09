package cz.kul.snippets.springboot.security_03_moreProviders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class ApplicationSecurity03 {

	/*

	When you need to have more Authentication Providers, create
	SecurityFilterChain by HttpSecurity builder. See below.

	Configure providers you need in the builder.

	*/
	public static void main(String[] args) {
		SpringApplication.run(ApplicationSecurity03.class, "--spring.config.name=security_03");
	}

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		return http
				.authenticationProvider(new CustomAuthenticationProvider("admin"))
				.authenticationProvider(new CustomAuthenticationProvider("user1"))
				.authenticationProvider(new CustomAuthenticationProvider("user2"))
				.httpBasic()
				.and()
				.build();
	}

}
