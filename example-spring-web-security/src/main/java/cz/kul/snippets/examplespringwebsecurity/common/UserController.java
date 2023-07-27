package cz.kul.snippets.examplespringwebsecurity.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationContext context;

	@GetMapping("/private/userDetails/{id}")
	public Object getUserDetails(@PathVariable("id") String id) {
		return Map.of(
				"id", id,
				"firstName", "Monica",
				"lastName", "Geller",
				"nickName", "moge001",
				"rc", "112233/0101",
				"age", 32);
	}

	@GetMapping("/public/user/{id}")
	public Object getUser(@PathVariable("id") String id) {
		return Map.of(
				"id", id,
				"nickName", "moge001");
	}

	@GetMapping("/private/securityContextDetails")
	public Object getSecurityContextDetails() {

		Set<String> authorities = null;
		Object principal = null;
		String principalClassName = null;

		SecurityContext context = SecurityContextHolder.getContext();
		if (context != null) {
			Authentication authentication = context.getAuthentication();
			if (authentication != null) {
				authorities = authentication.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				principal = authentication.getPrincipal();
				if (principal != null) {
					principalClassName = principal.getClass().getName();
				}
			}
		}

		LinkedHashMap<String, Object> res = new LinkedHashMap<>();
		res.put("principalClassName", principalClassName);
		res.put("principal", principal);
		res.put("authorities", authorities);
		return res;
	}


}
