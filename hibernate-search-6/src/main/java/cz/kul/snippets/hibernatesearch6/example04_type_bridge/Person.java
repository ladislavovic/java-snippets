package cz.kul.snippets.hibernatesearch6.example04_type_bridge;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Set;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String secondName;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private AttributeSet attributeSet;

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

	public AttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(AttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}

}
