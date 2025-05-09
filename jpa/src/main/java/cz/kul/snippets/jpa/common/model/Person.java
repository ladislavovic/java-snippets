package cz.kul.snippets.jpa.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private Date birthdate;

	public Person() {
	}

	public Person(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy="person")
	private Set<PersonDetail> details = new HashSet<>();

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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Set<PersonDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<PersonDetail> details) {
		this.details = details;
	}

	public void addDetail(PersonDetail detail) {
		details.add(detail);
		detail.setPerson(this);
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", birthdate=" + birthdate + "]";
	}

	@Override
	public boolean equals(final Object o)
	{
		if (!(o instanceof Person person)) {
			return false;
		}
        return id != null && Objects.equals(id, person.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(id);
	}

}
