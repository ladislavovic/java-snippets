package cz.kul.snippets.spring.example_25_resourceLoader;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class ResourceLoaderTest {

	@Test
	public void test() {
		ResourceLoader resourceLoader = new DefaultResourceLoader();

		Resource res1 = resourceLoader.getResource("classpath:log4j.properties");
		Assert.assertTrue(res1.exists());
		Assert.assertTrue(resourceToString(res1).length() > 10);

		Resource res2 = resourceLoader.getResource("file:/etc/passwd");
		Assert.assertTrue(res2.exists());
		Assert.assertTrue(resourceToString(res2).length() > 10);
	}

	private String resourceToString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


}
