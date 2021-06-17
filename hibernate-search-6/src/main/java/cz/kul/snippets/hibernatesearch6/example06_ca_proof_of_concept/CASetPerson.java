package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class CASetPerson extends CASet {

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "customAttributes")
	private Person person;

	public CASetPerson() {
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public Object getOwner() {
		return getPerson();
	}

	@Override
	public void setOwner(Object owner) {
		setPerson((Person) owner);
	}

}
