package cz.kul.snippets.gatewayapi1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MainController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	@GetMapping(value = "/operation1", produces = "application/json")
	public ResponseEntity operation1(HttpServletRequest request, Principal p) {

		LOGGER.info("/operation1 called");

		Map<String, Object> values = new HashMap<String, Object>();
		values.put("time", System.currentTimeMillis());
		values.put("backgroundServiceRequestURL", request.getRequestURL());
		values.put("user", p.getName());
		values.put("authorities", SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));

		return new ResponseEntity<>(values, HttpStatus.OK);
	}

	@GetMapping("/")
	public String root() {
		return "root";
	}

	@PostMapping("/operation2")
	public String operation2(HttpServletRequest request) {
		return "The POST operation called. Time: " + System.currentTimeMillis();
	}

}
