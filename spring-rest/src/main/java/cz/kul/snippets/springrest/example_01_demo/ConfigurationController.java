package cz.kul.snippets.springrest.example_01_demo;

import cz.kul.snippets.springrest.example_01_demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

	@PutMapping
	public Map<String, String> getConfiguration() {
		return null;
	}

}
