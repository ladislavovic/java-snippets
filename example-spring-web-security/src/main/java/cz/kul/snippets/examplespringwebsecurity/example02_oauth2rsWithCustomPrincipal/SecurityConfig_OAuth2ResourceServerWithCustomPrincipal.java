package cz.kul.snippets.examplespringwebsecurity.example02_oauth2rsWithCustomPrincipal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@EnableWebSecurity
@Profile("example02")
public class SecurityConfig_OAuth2ResourceServerWithCustomPrincipal {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .cors()
            .and()
            .authorizeRequests(auth -> auth.anyRequest().authenticated())
//          .access("hasAuthority('SCOPE_booking-calendar.read')")
//          .mvcMatchers("/update-booking-calendar/**")
//          .access("hasAuthority('SCOPE_booking-calendar.write')")
            .oauth2ResourceServer(httpSecurity -> httpSecurity
                .jwt()
                .jwtAuthenticationConverter(new MyJwtAuthConverter()))
            .build();
    }


    // TODO is it the right way how to switch off session?
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    public static class CustomUserDetailsService implements UserDetailsService {

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return new UserDetailsImpl(username, new HashSet<>());
        }
    }


}