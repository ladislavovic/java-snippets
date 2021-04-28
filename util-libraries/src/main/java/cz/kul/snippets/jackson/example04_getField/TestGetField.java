package cz.kul.snippets.jackson.example04_getField;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class TestGetField {

	@Test
  public void test() throws IOException {
		String json = "{" +
				"\"strField\": \"val\", " +
				"\"numField\": 10, " +
				"\"nullField\": null, " +
				"\"objField\": {\"foo\": 10}, " +
				"\"arrField\": [\"val1\", \"val2\"]" +
				"}";
		JsonNode parent = new ObjectMapper().readTree(json);
		Assert.assertEquals("val", parent.get("strField").asText());
		Assert.assertEquals("10", parent.get("numField").asText());
		Assert.assertEquals("null", parent.get("nullField").asText());
		Assert.assertEquals("", parent.get("objField").asText());
		Assert.assertEquals("", parent.get("arrField").asText());
  }

}
