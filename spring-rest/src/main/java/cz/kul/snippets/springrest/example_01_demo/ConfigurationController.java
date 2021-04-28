package cz.kul.snippets.springrest.example_01_demo;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

	@PutMapping
	public Map<String, String> getConfiguration() {
		return null;
	}

}
