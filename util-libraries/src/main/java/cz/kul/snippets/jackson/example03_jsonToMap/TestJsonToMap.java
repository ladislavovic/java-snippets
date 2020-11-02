package cz.kul.snippets.jackson.example03_jsonToMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class TestJsonToMap {

	@Test
	public void jsonToMap() throws IOException {
		String json = "{\"a\": \"val1\", \"b\": 10, \"c\": 10.1, \"d\": true, \"e\": null}";
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(json, Map.class);

		Assert.assertEquals(map.get("a"), "val1");
		Assert.assertEquals(map.get("b"), 10);
		Assert.assertEquals(map.get("c"), 10.1);
		Assert.assertEquals(map.get("d"), true);
		Assert.assertEquals(map.get("e"), null);



	}

}
