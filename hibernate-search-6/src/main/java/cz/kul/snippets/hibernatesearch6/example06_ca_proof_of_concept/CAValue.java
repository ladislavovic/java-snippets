package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CAValue {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String value;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private CASet caSet;

	public CAValue() {
	}

	public CAValue(String name, String value) {
		this.name = name;
		this.value = value;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CASet getCaSet() {
		return caSet;
	}

	public void setCaSet(CASet caSet) {
		this.caSet = caSet;
	}

}
