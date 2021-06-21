package cz.kul.snippets.freemarker.example05_classpath_tpl;

import com.google.common.collect.ImmutableMap;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ClasspathTplTest {

	@Test
	public void testSimpleUse() throws IOException {
		String output = FreemarkerUtils.processClasspathTpl("simpleUse.tpl", ImmutableMap.of("name", "Jane"));
		Assert.assertEquals("Jane is here!", output);
	}

}
