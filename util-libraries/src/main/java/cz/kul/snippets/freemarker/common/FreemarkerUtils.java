package cz.kul.snippets.freemarker.common;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtils {

    public static Configuration getStandardConfiguration(StringTemplateLoader loader) {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.29) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

        cfg.setTemplateLoader(loader);

        // From here we will set the settings recommended for new projects. These
        // aren't the defaults for backward compatibilty.

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into TemplateException-s:
        cfg.setWrapUncheckedExceptions(true);

        // Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);

        return cfg;
    }
    
    // TODO implement by calling another process() method
    public static String process(String template, Object dataModel) {
        try {
            String TEMPLATE_NAME = "TPL1";
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate(TEMPLATE_NAME, template);
            Configuration cfg = FreemarkerUtils.getStandardConfiguration(stringLoader);
            Template tpl = cfg.getTemplate(TEMPLATE_NAME);
            StringWriter writer = new StringWriter();
            tpl.process(dataModel, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String process(Map<String, String> nameToTpl, String mainTpl, Object dataModel) {
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            for (String name : nameToTpl.keySet()) {
                stringLoader.putTemplate(name, nameToTpl.get(name));
            }
            Configuration cfg = FreemarkerUtils.getStandardConfiguration(stringLoader);
            Template tpl = cfg.getTemplate(mainTpl);
            StringWriter writer = new StringWriter();
            tpl.process(dataModel, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
