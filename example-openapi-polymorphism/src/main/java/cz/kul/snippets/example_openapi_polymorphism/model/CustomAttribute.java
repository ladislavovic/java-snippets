package cz.kul.snippets.example_openapi_polymorphism.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "CustomAttribute", description = "Custom attribute")
public class CustomAttribute {

	@Schema(name = "name of custom attribute")
	private String name;

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	private List<CustomAttributeValue> val;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CustomAttributeValue> getVal() {
		return val;
	}

	public void setVal(List<CustomAttributeValue> val) {
		this.val = val;
	}
}
