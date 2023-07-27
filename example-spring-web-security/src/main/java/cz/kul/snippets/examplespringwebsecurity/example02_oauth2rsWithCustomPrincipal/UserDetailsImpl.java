package cz.kul.snippets.examplespringwebsecurity.example02_oauth2rsWithCustomPrincipal;

import com.google.common.base.Preconditions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * Just an simple implementation of UserDetails interface.
 */
public class UserDetailsImpl implements UserDetails {

	private Collection<? extends GrantedAuthority> authorities;

	private String username;

	public UserDetailsImpl(String username, Collection<? extends GrantedAuthority> authorities) {
		Preconditions.checkArgument(StringUtils.hasText(username));
    Preconditions.checkArgument(authorities != null);
		this.username = username;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
