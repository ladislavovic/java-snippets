package cz.kul.snippets.hibernatesearch6.example09_typical_fields_with_bridge;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private int age;

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

	public List<String> getDegrees() {
		return Arrays.asList("Prof", "ing", "CsC");
//		return Arrays.asList("Prof");
	}

}
