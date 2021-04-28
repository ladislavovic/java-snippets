package cz.kul.snippets.jackson.example05_jsonassert;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class JsonAssertTest {

	@Test
	public void comparisonWhenTheOrderIsDifferent() throws Exception {
		String expected = "{\"id\": 10, \"name\": \"Monica\"}";
		String actual = "{\"name\": \"Monica\", \"id\": 10}";

		JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
		JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
	}

	@Test
	public void comparisonWhenActualHasMoreElements() throws Exception {
		String expected = "{\"id\": 10, \"name\": \"Monica\"}";
		String actual = "{\"name\": \"Monica\", \"id\": 10, \"age\": 30}";

		JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
	}

	@Test
	public void comparisonOfArrayWithDifferentOrder() throws Exception {
		String expected = "{\"arr\": [10, 20, 30]}";
		String actual = "{\"arr\": [20, 10, 30]}";

		JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
	}

}
