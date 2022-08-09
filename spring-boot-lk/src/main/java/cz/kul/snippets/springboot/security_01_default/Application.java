package cz.kul.snippets.springboot.security_01_default;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class Application {

	/*

	When spring security is on classpath Spring Boot automatically setup some basic security:
	  * authentication method is BASIC or FORM (it decides probably according to request headers)
	  * username is "user"
	  * password is randomly generated and printed to the log

	*/
	public static void main(String[] args) {
		SpringApplication.run(Application.class, "--spring.config.name=security_01_default");
	}

}
