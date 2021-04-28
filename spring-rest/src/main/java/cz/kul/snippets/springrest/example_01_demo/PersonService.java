package cz.kul.snippets.springrest.example_01_demo;

import cz.kul.snippets.springrest.example_01_demo.model.PersonOutputDto;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

	private List<PersonOutputDto> people;

	@PostConstruct
	private void init() {
		people = new ArrayList<>();
		people.add(new PersonOutputDto(1, "Monica"));
		people.add(new PersonOutputDto(2, "Rachel"));
		people.add(new PersonOutputDto(3, "Phoeboe"));
	}

	public List<PersonOutputDto> getAllPeople() {
		return people;
	}

	public PersonOutputDto getPersonById(long id) {
		for (PersonOutputDto personOutputDto : people) {
			if (personOutputDto.getId() == id) {
				return personOutputDto;
			}
		}
		return null;
	}

}
