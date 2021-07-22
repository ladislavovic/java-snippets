package cz.kul.snippets.hibernatesearch6.example10_reindexing_when_dependency_updated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Car> getMyCars() {
		PersonService personService = ApplicationContextProvider.getBean(PersonService.class);
		List<Car> myCars = personService.getOwnedCars(this);
		return myCars;
	}

}
