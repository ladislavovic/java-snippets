package cz.kul.snippets.springboot.externalLib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication(scanBasePackages = {"cz.kul.snippets.springboot.externalLib", "cz.kul.snippets.springbootlib"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, "--spring.config.name=externallib");
	}

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		return http
				.anonymous()
				.and().build();
	}

}
