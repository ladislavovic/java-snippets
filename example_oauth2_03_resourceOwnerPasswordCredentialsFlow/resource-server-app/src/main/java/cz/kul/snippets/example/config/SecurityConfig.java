package cz.kul.snippets.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .authorizeRequests()

            .anyRequest().authenticated()

//          .mvcMatchers("/booking-calendar/**")
//          .access("hasAuthority('SCOPE_booking-calendar.read')")
//          .mvcMatchers("/update-booking-calendar/**")
//          .access("hasAuthority('SCOPE_booking-calendar.write')")
          .and()
          .oauth2ResourceServer()
          .jwt();
        return http.build();
    }
}