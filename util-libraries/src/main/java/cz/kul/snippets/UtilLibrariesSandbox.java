package cz.kul.snippets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;

import java.io.IOException;

public class UtilLibrariesSandbox {

	private static class Foo {
		String name;
		Class<?> clazz;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}
	}

	public static void main(String[] args) throws IOException {
		String json = "{\"name\": \"aaa\", \"clazz\": \"java.lang.Integer\"}";
		ObjectMapper mapper = new ObjectMapper();
		Foo foo = mapper.readValue(json, Foo.class);
		Assert.assertEquals("aaa", foo.getName());
		Assert.assertEquals(Integer.class, foo.getClazz());
	}

}
