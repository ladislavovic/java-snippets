package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CASet {

	@Id
	@GeneratedValue
	private Long id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caSet", orphanRemoval = true)
	private Set<CAValue> values = new HashSet<>();

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "customAttributes")
	private Person person;

	public CASet() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Set<CAValue> getValues() {
		return values;
	}

	public void setValues(Set<CAValue> values) {
		this.values = values;
	}

}
