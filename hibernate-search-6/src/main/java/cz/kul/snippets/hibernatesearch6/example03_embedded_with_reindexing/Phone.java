package cz.kul.snippets.hibernatesearch6.example03_embedded_with_reindexing;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Phone {

	@Id
	@GeneratedValue
	private Long id;

	private String num;

	@ManyToOne(fetch = FetchType.LAZY)
	private Person person;

	public Phone() {
	}

	public Phone(String num, Person person) {
		this.num = num;
		this.person = person;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
