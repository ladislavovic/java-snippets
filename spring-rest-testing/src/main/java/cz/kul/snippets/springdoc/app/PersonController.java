package cz.kul.snippets.springdoc.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public List<Person> findAll() {
		return personService.getAllPeople();
	}

	@GetMapping(value = "/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return personService.getPersonById(id);
	}

}
