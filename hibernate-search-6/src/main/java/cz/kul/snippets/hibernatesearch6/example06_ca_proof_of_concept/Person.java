package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String secondName;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private CASetPerson customAttributes;

	public Person() {
	}

	public Person(String firstName, String secondName) {
		this.firstName = firstName;
		this.secondName = secondName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String surname) {
		this.secondName = surname;
	}

	public CASetPerson getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(CASetPerson customAttributes) {
		this.customAttributes = customAttributes;
	}

}
