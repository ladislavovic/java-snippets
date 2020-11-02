package cz.kul.snippets.springrest.example_01_demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cz.kul.snippets.springrest.example_01_demo.model.Person;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/*
 It is a simple annotation which joins @Controller and @ResponseBody. So results returned by methods
 in this classes are used as response body.
 */
@RestController
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping
	public List<Person> findAll() {
		return personService.getAllPeople();
	}

	@ApiOperation(value = "Delete Link", tags = "TAG1")
	@GetMapping(value = "/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return personService.getPersonById(id);
//		return RestPreconditions.checkFound(service.findById(id));
	}

}
