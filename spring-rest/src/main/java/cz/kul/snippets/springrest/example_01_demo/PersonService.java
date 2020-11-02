package cz.kul.snippets.springrest.example_01_demo;

import cz.kul.snippets.springrest.example_01_demo.model.Person;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

	private List<Person> people;

	@PostConstruct
	private void init() {
		people = new ArrayList<>();
		people.add(new Person(1, "Monica"));
		people.add(new Person(2, "Rachel"));
		people.add(new Person(3, "Phoeboe"));
	}

	public List<Person> getAllPeople() {
		return people;
	}

	public Person getPersonById(long id) {
		for (Person person : people) {
			if (person.getId() == id) {
				return person;
			}
		}
		return null;
	}

}
