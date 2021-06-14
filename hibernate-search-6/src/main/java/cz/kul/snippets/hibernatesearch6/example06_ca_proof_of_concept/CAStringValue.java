package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;

@Entity
public class CAStringValue extends CAValue<String>{

	private String stringValue;

	public CAStringValue() {
	}

	public CAStringValue(String name, String stringValue) {
		this.stringValue = stringValue;
		setName(name);
	}

	@Override
	public String getValue() {
		return stringValue;
	}

	@Override
	public void setValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String getStrValue() {
		return getValue();
	}

}
