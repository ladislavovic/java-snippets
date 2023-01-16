package cz.kul.snippets.example_openapi_polymorphism.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("string")
public class StringValue implements CustomAttributeValue {

	private String val;

	public StringValue() {
	}

	public StringValue(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
