package cz.kul.snippets.example_openapi_polymorphism.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Order", description = "Just an order")
public class Order {

	private long id;

	@Schema(description = "Custom attributes.")
	private List<CustomAttribute> customAttributes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<CustomAttribute> getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(List<CustomAttribute> customAttributes) {
		this.customAttributes = customAttributes;
	}

}
