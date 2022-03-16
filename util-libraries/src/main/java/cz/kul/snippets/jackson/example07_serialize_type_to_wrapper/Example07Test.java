package cz.kul.snippets.jackson.example07_serialize_type_to_wrapper;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Example07Test {

	@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
	@JsonSubTypes(value = {
			@JsonSubTypes.Type(value = Constant.class, name = "constant"),
			@JsonSubTypes.Type(value = ByNodeType.class, name = "byNodeType")
	})
	static abstract class WeightFunction {
		private int boost = 1;

		public int getBoost() {
			return boost;
		}

		public void setBoost(int boost) {
			this.boost = boost;
		}
	}

	static class Constant extends WeightFunction {
		private int value = 1;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	static class ByNodeType extends WeightFunction {
		private Map<String, Long> values;

		public ByNodeType() {
		}

		public ByNodeType(Map<String, Long> values) {
			this.values = values;
		}

		public Map<String, Long> getValues() {
			return values;
		}

		public void setValues(Map<String, Long> values) {
			this.values = values;
		}
	}

	static class Criteria {

		private List<WeightFunction> weightFunctions;

		public List<WeightFunction> getWeightFunctions() {
			return weightFunctions;
		}

		public void setWeightFunctions(List<WeightFunction> weightFunctions) {
			this.weightFunctions = weightFunctions;
		}
	}

	@Test
	public void testSerialization() throws Exception{
		Criteria criteria = new Criteria();
		criteria.setWeightFunctions(Arrays.asList(
				new Constant(),
				new ByNodeType(ImmutableMap.of("RACK", 4L, "ODF", 8L))
		));

		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		System.out.println(objectMapper.writeValueAsString(criteria));
	}

	@Test
	public void testDeserialization() throws Exception {
		String input = "{\n" +
				"  \"weightFunctions\" : [ {\n" +
				"    \"constant\" : {\n" +
				"      \"boost\" : 1,\n" +
				"      \"value\" : 1\n" +
				"    }\n" +
				"  }, {\n" +
				"    \"byNodeType\" : {\n" +
				"      \"boost\" : 1,\n" +
				"      \"values\" : {\n" +
				"        \"RACK\" : 4,\n" +
				"        \"ODF\" : 8\n" +
				"      }\n" +
				"    }\n" +
				"  } ]\n" +
				"}";

		ObjectMapper objectMapper = new ObjectMapper();
		Criteria criteria = objectMapper.readValue(input, Criteria.class);
		Assert.assertEquals(2, criteria.getWeightFunctions().size());
		Assert.assertEquals(Constant.class, criteria.getWeightFunctions().get(0).getClass());
		Assert.assertEquals(ByNodeType.class, criteria.getWeightFunctions().get(1).getClass());
	}

}
