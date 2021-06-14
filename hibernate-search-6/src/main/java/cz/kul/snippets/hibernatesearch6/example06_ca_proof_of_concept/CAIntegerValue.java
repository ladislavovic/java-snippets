package cz.kul.snippets.hibernatesearch6.example06_ca_proof_of_concept;

import javax.persistence.Entity;

@Entity
public class CAIntegerValue extends CAValue<Integer>{

	private Integer integerValue;

	public CAIntegerValue() {
	}

	public CAIntegerValue(String name, Integer integerValue) {
		this.integerValue = integerValue;
		setName(name);
	}

	@Override
	public Integer getValue() {
		return integerValue;
	}

	@Override
	public void setValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	@Override
	public String getStrValue() {
		return integerValue == null ? null : integerValue.toString();
	}

}
