package cz.kul.snippets.example.oauth2.swaggerui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityContextConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.anyRequest().authenticated()
				.and()
				.oauth2ResourceServer()
				.jwt();
		return http.build();

//		http
//				.authorizeRequests()
//				.mvcMatchers("/booking-calendar/**")
//				.access("hasAuthority('SCOPE_booking-calendar.read')")
//				.mvcMatchers("/update-booking-calendar/**")
//				.access("hasAuthority('SCOPE_booking-calendar.write')")
//				.and()
//				.oauth2ResourceServer()
//				.jwt();
//		return http.build();
	}

}
