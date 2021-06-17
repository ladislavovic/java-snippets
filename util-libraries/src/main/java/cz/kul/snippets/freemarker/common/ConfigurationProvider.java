package cz.kul.snippets.freemarker.common;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;

/**
 * Configuration is a factory for Templates. Typically you have
 * one instance of Configuration in runtime.
 *
 * Then you typically call getTemplate() methods. Configuration also cache templates
 * so you do not have to store already given templates somewhere else.
 */
public class ConfigurationProvider {

	private static Configuration configuration;

	static {
		configuration = new Configuration(Configuration.VERSION_2_3_29);

		// Templates will be loaded from the "GivenClassPackage/templates"
		ClassTemplateLoader templateLoader = new ClassTemplateLoader(ConfigurationProvider.class, "templates");
		configuration.setTemplateLoader(templateLoader);

		// Encoding the templates are stored in
		configuration.setDefaultEncoding("UTF-8");
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

}
