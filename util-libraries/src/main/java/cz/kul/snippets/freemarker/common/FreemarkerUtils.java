package cz.kul.snippets.freemarker.common;

import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtils {

    /**
     * Configuration is a factory for Templates. Typically you have
     * one instance of Configuration in runtime. But here in examples it is it works in different way.
     *
     * Then you typically call getTemplate() methods. Configuration also cache templates
     * so you do not have to store already given templates somewhere else.
     */
    public static Configuration getConfiguration(TemplateLoader loader) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");
        return cfg;
    }

    public static String processClasspathTpl(String tplName, Object dataModel) {
        try {
            ClassTemplateLoader templateLoader = new ClassTemplateLoader(FreemarkerUtils.class, "templates");
            Configuration cfg = FreemarkerUtils.getConfiguration(templateLoader);
            return doProcessTpl(cfg, tplName, dataModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String processStrTpl(String template, Object dataModel) {
        String tplName = "TPL1";
        return processStrTpl(ImmutableMap.of(tplName, template), tplName, dataModel);
    }
    
    public static String processStrTpl(Map<String, String> nameToTpl, String mainTpl, Object dataModel) {
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            for (String name : nameToTpl.keySet()) {
                stringLoader.putTemplate(name, nameToTpl.get(name));
            }
            Configuration cfg = FreemarkerUtils.getConfiguration(stringLoader);
            return doProcessTpl(cfg, mainTpl, dataModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String doProcessTpl(Configuration cfg, String tplName, Object dataModel) throws Exception{
        Template tpl = cfg.getTemplate(tplName);
        StringWriter writer = new StringWriter();
        tpl.process(dataModel, writer);
        return writer.toString();
    }

}
