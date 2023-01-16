package cz.kul.snippets.example_openapi_polymorphism.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
		name = "CA value",
		subTypes = {
				RefValue.class,
				StringValue.class,
				NumberValue.class
		})
public interface CustomAttributeValue {
}
