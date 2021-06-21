package cz.kul.snippets.freemarker.example04_include;

import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestInclude {

    @Test
    public void basicInclude() {
        String main = String.join(
                "\n",
                "A<#include \"incl\">A"
        );
        String incl = "included";

        Map<String, String> templates = new HashMap<>();
        templates.put("main", main);
        templates.put("incl", incl);

        String res = FreemarkerUtils.processStrTpl(templates, "main", new Object());
        assertEquals("AincludedA", res);
    }
    
    @Test
    public void includeWithParam() {
        String main = String.join(
                "\n",
                "<#assign year=\"2020\">",
                "A<#include \"copyright\">A"
        );
        String copyright = "Copyright ${year}";

        Map<String, String> templates = new HashMap<>();
        templates.put("main", main);
        templates.put("copyright", copyright);

        String res = FreemarkerUtils.processStrTpl(templates, "main", new Object());
        assertEquals("ACopyright 2020A", res);
    }
    
    @Test
    public void includeWithTeplatesInResources() throws Exception {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration();
        cfg.setClassForTemplateLoading(TestInclude.class, "");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template tpl = cfg.getTemplate("main.tpl");
        StringWriter writer = new StringWriter();
        tpl.process(new Object(), writer);
        
        assertEquals("AINCA", writer.toString());
    }

}
