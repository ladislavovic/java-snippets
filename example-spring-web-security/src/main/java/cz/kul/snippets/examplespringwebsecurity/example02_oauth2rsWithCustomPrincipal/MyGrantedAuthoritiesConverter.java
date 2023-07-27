package cz.kul.snippets.examplespringwebsecurity.example02_oauth2rsWithCustomPrincipal;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * By default, Spring translates scopes to Granted Authorities. This custom converter
 * ignore scopes and create Granted Authorities from "claims.realm_access.roles". In Keycloak
 * it contains items from "Service accounts roles".
 */
public class MyGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(final Jwt jwt) {

		final Object realmAccessClaim = jwt.getClaims().get("realm_access");
		if (!(realmAccessClaim instanceof Map)) {
			System.err.println("ERROR: JWT token does not contain claim 'realm_access' or it is in unexpected format. Can not get user authorities.");
			return Collections.emptyList();
		}

		final Map<String, Object> realmAccess = (Map<String, Object>) realmAccessClaim;
		Object roles = realmAccess.get("roles");
		if (!(roles instanceof List)) {
			System.err.println("ERROR: The JWT token claim 'realm_access' does not contains 'roles' or they are in unexpected format. Can not get user authorities.");
			return Collections.emptyList();
		}

		return ((List<String>) roles).stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

}
