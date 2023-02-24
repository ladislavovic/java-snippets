package cz.kul.snippets.spring.example_22_value;

import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestValueList {

	@Configuration
	@PropertySource(
			name = "propertyPlaceholderConfigurer",
			value = {"classpath:example22.properties"},
			ignoreResourceNotFound = false)
	public static class Conf {

		// It works out of the box. It also trim whitespaces.
		@Value("${numbers}")
		private String[] stringArr;

		// When the property is empty it set it as an empty array. NOT null.
		@Value("${numbersEmpty}")
		private String[] stringArr2;

		// I do not know, how to inject list here
		private int[] intArr;

		// I do not know, how to inject list here
		private Integer[] integerArr;

		// To inject it to List yo must use SpEl. This expresion does not trim white characters.
		@Value("#{'${numbers}'.split(',')}")
		private List<String> stringList;

		// To trim whitespaces you must use the following split
		@Value("#{'${numbers}'.split('\\s*,\\s*')}")
		private List<String> stringList2;

		// This way you can also insert to Integer list out of the box
		@Value("#{'${numbers}'.split('\\s*,\\s*')}")
		private List<Integer> integerList;

		public List<Integer> getIntegerList() {
			return integerList;
		}

		public List<String> getStringList() {
			return stringList;
		}

		public List<String> getStringList2() {
			return stringList2;
		}

		public int[] getIntArr() {
			return intArr;
		}

		public Integer[] getIntegerArr() {
			return integerArr;
		}

		public String[] getStringArr() {
			return stringArr;
		}

		public String[] getStringArr2() {
			return stringArr2;
		}
	}

	@Test
	public void valueList() {
		SpringTestUtils.runInSpring(TestValueList.Conf.class, ctx -> {
			Conf conf = ctx.getBean(Conf.class);

			assertArrayEquals( new String[] {"1", "2", "3"}, conf.getStringArr());

			assertArrayEquals(new String[] {}, conf.getStringArr2());

			assertEquals(Arrays.asList("1", " 2 ", "3"), conf.getStringList());

			assertEquals(Arrays.asList("1", "2", "3"), conf.getStringList2());

			assertEquals(Arrays.asList(1, 2, 3), conf.getIntegerList());

		});
	}


}
