package cz.kul.snippets.example.oauth2.swaggerui;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "security_auth")
public class PersonController {

	@GetMapping("/person/{id}")
	public String getPerson(@PathVariable("id") Long id) {
		return "Person \"" + id + "\"";
	}

}
