package cz.kul.snippets.springdoc.app;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
@Tag(name = "Person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@Operation(summary = "Find all people", tags = "Person")
	@GetMapping
	public List<Person> findAll() {
		return personService.getAllPeople();
	}

	@Operation(summary = "Find person by ID", tags = "Person")
	@GetMapping(value = "/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return personService.getPersonById(id);
	}

}
