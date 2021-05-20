package cz.kul.snippets.yaml.example01_hw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class TestHw {

	@Test
	public void test() throws IOException {
		InputStream yamlStream = this.getClass().getResourceAsStream("/order.yaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Order order = mapper.readValue(yamlStream, Order.class);
		Assert.assertEquals("A001", order.getOrderNo());
	}

}
