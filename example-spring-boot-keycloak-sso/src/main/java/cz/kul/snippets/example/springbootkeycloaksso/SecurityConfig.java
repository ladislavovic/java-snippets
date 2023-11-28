package cz.kul.snippets.example.springbootkeycloaksso;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	private KeycloakLogoutHandler keycloakLogoutHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.authorizeRequests(authorizeRequests ->
						authorizeRequests.antMatchers("/secured").authenticated())
				.oauth2Login(withDefaults())
				  .oauth2Login(withDefaults())
				  .logout(logout -> logout
							 .addLogoutHandler(keycloakLogoutHandler)
//							 .logoutSuccessUrl("/")
//							 .logoutSuccessHandler(oidcLogoutSuccessHandler())
				  );

		return http.build();
	}

	private LogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
				  new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

		// Sets the location that the End-User's User Agent will be redirected to
		// after the logout has been performed at the Provider
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

		return oidcLogoutSuccessHandler;
	}



}
