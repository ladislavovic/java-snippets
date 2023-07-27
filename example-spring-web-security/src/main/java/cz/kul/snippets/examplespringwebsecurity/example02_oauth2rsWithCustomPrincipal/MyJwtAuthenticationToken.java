package cz.kul.snippets.examplespringwebsecurity.example02_oauth2rsWithCustomPrincipal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;

/**
 * Custom authentication token created according to {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken}
 *
 * I can not use directly {@link org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken}, because it set Jwt as principal and there is no way how to
 * change it.
 */
public class MyJwtAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

	private static final long serialVersionUID = 1L;

	private final String name;

	protected MyJwtAuthenticationToken(
			Jwt token,
			Object principal,
			Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(token, principal, credentials, authorities);
		this.name = token.getSubject();
		this.setAuthenticated(true);
	}

	@Override
	public Map<String, Object> getTokenAttributes() {
		return this.getToken().getClaims();
	}

	@Override
	public String getName() {
		return this.name;
	}

}
