package cz.kul.snippets.gatewayapi1;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {

	@GetMapping("/operation1")
	@Operation(summary = "Backend api 1 operation")
	public String operation1(HttpServletRequest request) {
		return "The result (backend1):\n" +
				"time: " + System.currentTimeMillis() + "\n" +
				"URL: " + request.getRequestURL();
	}

}
