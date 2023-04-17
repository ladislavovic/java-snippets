package cz.kul.snippets.gatewayapi2;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {

	@GetMapping("/operation2")
	@Operation(summary = "Backend api 2 operation")
	public String operation1(HttpServletRequest request) {
		return "The result (backend2):\n" +
				"time: " + System.currentTimeMillis() + "\n" +
				"URL: " + request.getRequestURL();
	}

}
