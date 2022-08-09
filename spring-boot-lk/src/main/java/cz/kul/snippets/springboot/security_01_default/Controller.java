package cz.kul.snippets.springboot.security_01_default;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

}
