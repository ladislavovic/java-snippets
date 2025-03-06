package cz.kul.snippets.springboot.security_01_default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@Autowired
	MyService myService;

	@GetMapping("/hello")
	public String hello()
	{
		try {
			myService.getMeSecredData();
		} catch (Exception e) {
			e.printStackTrace(); // Pre authorize does not wrap the exception. Great!
		}
		return "hello";
	}

}
