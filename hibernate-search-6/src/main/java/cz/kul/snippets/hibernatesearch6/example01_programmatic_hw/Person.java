package cz.kul.snippets.hibernatesearch6.example01_programmatic_hw;

import javax.persistence.*;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String surname;

	private String sex;

	public Person() {
	}

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getWholeName() {
		return String.format("%s %s", getName(), getSurname());
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
	}

}
