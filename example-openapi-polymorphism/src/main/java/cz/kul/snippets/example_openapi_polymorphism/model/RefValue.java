package cz.kul.snippets.example_openapi_polymorphism.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ref")
public class RefValue implements CustomAttributeValue {
	private String externalId;
	private String systemId;

	public RefValue(String externalId, String systemId) {
		this.externalId = externalId;
		this.systemId = systemId;
	}

	public RefValue() {
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
