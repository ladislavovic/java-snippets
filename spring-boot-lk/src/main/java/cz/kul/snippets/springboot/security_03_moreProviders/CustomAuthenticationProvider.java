package cz.kul.snippets.springboot.security_03_moreProviders;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final String username;

	public CustomAuthenticationProvider(String username) {
		this.username = username;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (String.valueOf(authentication.getPrincipal()).equalsIgnoreCase(username)) {
			return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
