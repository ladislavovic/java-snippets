package cz.kul.snippets.jackson.example06_mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MappingTest {

	@Test
	public void objToJson() throws Exception {
		List<String> strings = Arrays.asList("val1", "val2");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(strings);
		Assert.assertEquals("[\"val1\",\"val2\"]", json);
	}

	@Test
	public void jsonNodeToJson() throws Exception {
		TextNode node = new TextNode("node1");
		String json = (new ObjectMapper()).writeValueAsString(node);
		Assert.assertEquals("\"node1\"", json);
	}

	@Test
	public void jsonToNode() throws Exception {
		String json = "{\"id\": 10, \"name\": \"Monica\"}";
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);

		Assert.assertTrue(jsonNode.isObject());
		Assert.assertTrue(jsonNode.hasNonNull("id") && jsonNode.hasNonNull("name"));
	}

}
