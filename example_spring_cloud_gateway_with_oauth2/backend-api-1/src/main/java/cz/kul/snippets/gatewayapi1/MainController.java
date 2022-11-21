package cz.kul.snippets.gatewayapi1;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {

	@GetMapping("/operation1")
	public String operation1(HttpServletRequest request, @AuthenticationPrincipal Jwt jwt) {
		String tokenValue = jwt != null ? jwt.getTokenValue() : "";

		return "The result:\n" +
				"time: " + System.currentTimeMillis() + " \n" +
				"URL: " + request.getRequestURL() + " \n" +
				"JWT: " + tokenValue;
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
