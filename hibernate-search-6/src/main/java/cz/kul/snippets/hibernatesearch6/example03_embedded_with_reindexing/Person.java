package cz.kul.snippets.hibernatesearch6.example03_embedded_with_reindexing;

import org.hibernate.search.util.common.SearchTimeoutException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String surname;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true)
	private Set<Phone> phones = new HashSet<>();

	public Person() {
	}

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
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

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", surname=" + surname + "]";
	}

}
