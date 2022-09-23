package cz.kul.snippets.springbootlib.feature1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Feature1Controller {

	public Feature1Controller() {
		System.out.println("\n\nCREATED A\n\n");
	}
	
	@GetMapping("/feature1/operation1")
	public String operation1() {
		return "operation1 result";
	}

	@GetMapping("/feature1/operation2")
	public String operation2() {
		return "operation2 result";
	}

}
