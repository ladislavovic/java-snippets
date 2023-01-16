package cz.kul.snippets.jackson.example09_serialize_type_to_parent_attribute;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import cz.kul.snippets.jackson.example08_serialize_type_to_attribute.Example08Test;
import org.junit.Test;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Example which saves instance type as instance property.
 */
public class Example09Test {


	@Test
	public void testSerialization() throws Exception{
		CustomAttribute ca = new CustomAttribute();
		ca.setName("ca1");
		ca.setVal(ImmutableList.of(
				new StringValue("foo"),
				new NumberValue(42),
				new RefValue("AAA-BBB-CCC", "CROSS")));

		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		System.out.println(objectMapper.writeValueAsString(ca));
	}

	static class CustomAttribute {

		private String name;

		// use - sepecity which data will be used as type identifier. It can be "class" or "name" or some custom value.
		// include - specify how to store type into json. It cann be property, parent property, wrapper, ...
		// property - name of property which contains type information. NOTE: it should not be defined in POJO,
		//            it is created by jackson.
		@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
		private List<Object> val;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Object> getVal() {
			return val;
		}

		public void setVal(List<Object> val) {
			this.val = val;
		}
	}

	@JsonTypeName("ref")
	public static class RefValue {
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

	@JsonTypeName("string")
	public static class StringValue {

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

	@JsonTypeName("number")
	public static class NumberValue {

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

}
