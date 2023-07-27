package cz.kul.snippets.examplespringwebsecurity.example02_oauth2rsWithCustomPrincipal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * The converter which converts {@link Jwt} to {@link org.springframework.security.core.Authentication}.
 *
 * Spring can do it automatically, but when you want to customize this conversion you
 * must create your own converter.
 *
 * Customizations done by this converter:
 *   - creates authorities from "Service account roles" (by default it is from OAuth2 scopes)
 *   - set UserDetail as principal (by default Jwt is a principal)
 *
 */
public class MyJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private JwtAuthenticationConverter jwtAuthenticationConverter;

	public MyJwtAuthConverter() {
		this.jwtAuthenticationConverter = new JwtAuthenticationConverter();
		this.jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyGrantedAuthoritiesConverter());
	}

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		// Creates a token by standard Spring converter
		JwtAuthenticationToken token = (JwtAuthenticationToken) jwtAuthenticationConverter.convert(jwt);

		// Create a custom principal
		UserDetailsImpl principal = new UserDetailsImpl(jwt.getSubject(), token.getAuthorities());

		// Create a custom token with my custom principal
		return new MyJwtAuthenticationToken(
				token.getToken(),
				principal,
				token.getCredentials(),
				token.getAuthorities());
	}

}
