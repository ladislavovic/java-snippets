package cz.kul.snippets.freemarker.example05_simple_use;

import com.google.common.collect.ImmutableMap;
import cz.kul.snippets.freemarker.common.ConfigurationProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class SimpleUseTest {

	@Test
	public void testSimpleUse() throws IOException {
		try(StringWriter writer = new StringWriter()) {
			// freemarker does not close the writer, you have to do it yourself.
			processTemplate("simpleUse.tpl", ImmutableMap.of("name", "Jane"), writer);
			Assert.assertEquals("Jane is here!", writer.toString());
		}
	}

	/**
	 * @param templateName name of the template
	 * @param dataModel    usually map or java bean
	 * @param writer       just the writer
	 */
	private void processTemplate(String templateName, Object dataModel, Writer writer) {
		try {
			Configuration conf = ConfigurationProvider.getConfiguration();
			Template tpl = conf.getTemplate("simpleUse.tpl");
			tpl.process(dataModel, writer);
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


}
