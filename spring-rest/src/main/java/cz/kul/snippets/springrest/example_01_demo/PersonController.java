package cz.kul.snippets.springrest.example_01_demo;

import cz.kul.snippets.springrest.example_01_demo.config.SpringfoxContextConfiguration;
import cz.kul.snippets.springrest.example_01_demo.model.PersonInputDto;
import cz.kul.snippets.springrest.example_01_demo.model.PersonOutputDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/*
 It is a simple annotation which joins @Controller and @ResponseBody. So results returned by methods
 in this classes are used as response body.
 */
@RestController
@Api(tags = SpringfoxContextConfiguration.TAG1)
@RequestMapping("/person")
public class PersonController {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private PersonService personService;

	@GetMapping
	public List<PersonOutputDto> findAll() {
		return personService.getAllPeople();
	}

	@ApiOperation(
			value = "Get person by id.", // 'summary' in swagger json
			notes = // 'description ' in swagger json. Markdown can be used here
					"some **operation** description here.\n\n" +
							"col1 | col2\n" +
							"---- | ----\n" +
							"row1 | row1\n" +
							"row2 | row2\n")
	@GetMapping(value = "/{id}")
	public PersonOutputDto findById(@PathVariable("id") Long id) {

		Map<String, RequestMappingHandlerMapping> beans = ctx.getBeansOfType(RequestMappingHandlerMapping.class);
		System.out.println(beans);

		return personService.getPersonById(id);
//		return RestPreconditions.checkFound(service.findById(id));
	}


	@ApiOperation(value = "Create a new Person")
	@PostMapping(value = "/")
	public void createPerson(@RequestBody PersonInputDto personInputDto) {

	}

}
