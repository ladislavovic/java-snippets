package cz.kul.snippets.springboot.security_02_customAuthenticationProvider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

}
