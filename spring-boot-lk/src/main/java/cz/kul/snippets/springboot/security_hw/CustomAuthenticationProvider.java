package cz.kul.snippets.springboot.security_hw;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (String.valueOf(authentication.getPrincipal()).equalsIgnoreCase("admin")) {
			return new UsernamePasswordAuthenticationToken("admin", null, Collections.emptyList());
		}
		throw new UsernameNotFoundException("Username not found");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
