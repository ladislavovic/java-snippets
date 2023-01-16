package cz.kul.snippets.example_openapi_polymorphism.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("number")
public class NumberValue implements CustomAttributeValue {

	private Number val;

	public NumberValue() {
	}

	public NumberValue(Number val) {
		this.val = val;
	}

	public Number getVal() {
		return val;
	}

	public void setVal(Number val) {
		this.val = val;
	}

}
