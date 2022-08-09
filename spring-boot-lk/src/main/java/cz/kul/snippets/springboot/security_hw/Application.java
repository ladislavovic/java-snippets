package cz.kul.snippets.springboot.security_hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider();
	}

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		return http
				.csrf().disable()
				.httpBasic()
				.and().authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.build();
	}

}
