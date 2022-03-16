package cz.kul.snippets.spring.example_22_value;

import com.google.common.collect.Lists;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestValueList {

	@Configuration
	@PropertySource(
			name = "propertyPlaceholderConfigurer",
			value = {"classpath:example22.properties"},
			ignoreResourceNotFound = true)
	public static class Conf {

		@Value("#{'${agreements}'.split(',')}")
		private List<String> agreements;

		public List<String> getAgreements() {
			return agreements;
		}

	}

	@Test
	public void valueList() {
		SpringTestUtils.runInSpring(TestValueList.Conf.class, ctx -> {
			Conf conf = ctx.getBean(Conf.class);

			// NOTES - it does not trim white characters
			assertEquals(Arrays.asList("hi", " hello"), conf.getAgreements());
		});
	}


}
