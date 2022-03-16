package cz.kul.snippets.jackson.example08_serialize_type_to_attribute;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

public class Example08Test {

	static interface WeightFunctionData {
	}

	static class ConstantWeightFunctionData implements WeightFunctionData {
		private int value = 1;

		public ConstantWeightFunctionData() {
		}

		public ConstantWeightFunctionData(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	static class ByNodeTypeWeightFunctionData implements WeightFunctionData {
		private Map<String, Long> values;

		public ByNodeTypeWeightFunctionData() {
		}

		public ByNodeTypeWeightFunctionData(Map<String, Long> values) {
			this.values = values;
		}

		public Map<String, Long> getValues() {
			return values;
		}

		public void setValues(Map<String, Long> values) {
			this.values = values;
		}
	}

	static class WeightFunction {

		private String weightFunctionName;

		@JsonTypeInfo(include = As.EXTERNAL_PROPERTY, use = Id.NAME, property = "weightFunctionName")
		@JsonSubTypes(value = {
				@JsonSubTypes.Type(value = ConstantWeightFunctionData.class, name = "CONSTANT"),
				@JsonSubTypes.Type(value = ByNodeTypeWeightFunctionData.class, name = "BY_NODE_TYPE")
		})
		private WeightFunctionData data;

		public String getWeightFunctionName() {
			return weightFunctionName;
		}

		public void setWeightFunctionName(String weightFunctionName) {
			this.weightFunctionName = weightFunctionName;
		}

		public WeightFunctionData getData() {
			return data;
		}

		public void setData(WeightFunctionData data) {
			this.data = data;
		}
	}

	@Test
	public void testSerialization() throws Exception{
		WeightFunction weightFunction = new WeightFunction();
		weightFunction.setWeightFunctionName("CONSTANT");
		weightFunction.setData(new ConstantWeightFunctionData(5));

		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		System.out.println(objectMapper.writeValueAsString(weightFunction));
	}

	@Test
	public void testDeserialization() throws Exception {
		String input = "{\n" +
				"  \"weightFunctionName\" : \"CONSTANT\",\n" +
				"  \"data\" : {\n" +
				"    \"value\" : 5\n" +
				"  }\n" +
				"}";

		ObjectMapper objectMapper = new ObjectMapper();
		WeightFunction weightFunction = objectMapper.readValue(input, WeightFunction.class);
		Assert.assertEquals(ConstantWeightFunctionData.class, weightFunction.getData().getClass());
	}

}
