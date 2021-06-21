package cz.kul.snippets.freemarker.example01_tpl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExampleTpl {

    @Test
    public void binding() {
        String res = FreemarkerUtils.processStrTpl("Hello world, ${user}!", ImmutableMap.of("user", "Monica"));
        assertEquals("Hello world, Monica!", res);
    }
    
    @Test
    public void listFTL() {
        String template = String.join(
                "\n",
                "<#list fruits as fruit>",
                "${fruit}",
                "</#list>"
        );

        ArrayList<String> fruits = Lists.newArrayList("banana", "orange");

        String res = FreemarkerUtils.processStrTpl(template, ImmutableMap.of("fruits", fruits));
        assertEquals("banana\norange\n", res);
    }

    @Test
    public void maps() {
        ImmutableMap<String, Object> dataModel = ImmutableMap.of(
            "map", ImmutableMap.of(
              "simpleVal", "banana",
              "listVal", Arrays.asList("banana", "orange"),
              "mapVal", ImmutableMap.of("fruit", "banana")));

        {
            // iterate keys
            String out = FreemarkerUtils.processStrTpl("<#list map?keys as key>${key} </#list>", dataModel);
            Assert.assertEquals("simpleVal listVal mapVal ", out);
        }

        {
            // iterate values
            String out = FreemarkerUtils.processStrTpl("<#list map.mapVal?values as val>${val} </#list>", dataModel);
            Assert.assertEquals("banana ", out);
        }

        {
            // get particular value - static
            String out = FreemarkerUtils.processStrTpl("<#list map.listVal as val>${val} </#list>", dataModel);
            Assert.assertEquals("banana orange ", out);
        }

        {
            // get particular value - dynamic
            String out = FreemarkerUtils.processStrTpl("<#list map.mapVal?keys as key>${key}=${map.mapVal[key]}</#list>", dataModel);
            Assert.assertEquals("fruit=banana", out);
        }

    }

    @Test
    public void emptyList() {
        String template = String.join(
                "\n",
                "<#list fruits as fruit>",
                "${fruit}",
                "</#list>"
        );

        ArrayList<String> fruits = Lists.newArrayList();

        String res = FreemarkerUtils.processStrTpl(template, ImmutableMap.of("fruits", fruits));
        assertEquals("", res);
    }

    @Test
    public void nullValueCauseError() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("user", null);
        try {
            FreemarkerUtils.processStrTpl("${user}", model);
            Assert.fail("exception expected");
        } catch (Exception e) {
        }

        String res = FreemarkerUtils.processStrTpl("${user!\"anonymous\"}", model);
        assertEquals("anonymous", res);
    }
    
    
    @Test
    public void missingValueCauseError() {
        try {
            FreemarkerUtils.processStrTpl("${user}!", Maps.newHashMap());
            Assert.fail("exception expected");
        } catch (Exception e) {
        }

        String res = FreemarkerUtils.processStrTpl("${user!\"anonymous\"}", Maps.newHashMap());
        assertEquals("anonymous", res);
    }
    
    @Test
    public void buildIns() {
        String tpl = String.join(
                ",",
                "${user}",
                "${user?upper_case}",
                "${user?cap_first}",
                "${user?length}"
        );

        String res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("monica,MONICA,Monica,6", res);
    }
    
    @Test
    public void testMissingValues() {
        String tpl = "<#if user??>${user}</#if>";
        
        String res = FreemarkerUtils.processStrTpl(tpl, new HashMap<>());
        assertEquals("", res);
        
        res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("monica", res);
    }

    @Test
    public void testEmptyValue() {
        String tpl = "<#if user?has_content>A${user}A</#if>";

        String res = FreemarkerUtils.processStrTpl(tpl, new HashMap<>());
        assertEquals("", res);

        res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", ""));
        assertEquals("", res);

        res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("AmonicaA", res);
    }
    
    @Test
    public void setCanBeAlsoAccessedByIndex() {
        String tpl = "${names[0]}";
        
        String res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("names", Sets.newHashSet("monica", "rachel")));
        assertTrue("monica".equals(res) || "rachel".equals(res));
    }
    
    @Test
    public void unassign() {
        String tpl = "<#assign foo=true><#if foo>true</#if>";
        
        String res = FreemarkerUtils.processStrTpl(tpl, new Object());
        assertEquals("true", res);
    }

    @Test
    public void stringComparison() {
        String tpl = "<#if user == \"monica\" || user == \"rachel\">match</#if>";

        String res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", ""));
        assertEquals("", res);

        res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("match", res);
        
        res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("user", "rachel"));
        assertEquals("match", res);
    }

    @Test
    public void numberFormat() {
        String tpl = "${num}";

        String res = FreemarkerUtils.processStrTpl(tpl, ImmutableMap.of("num", 1000));
        assertEquals("1000", res);
    }

    // so calling a method of the java bean
    @Test
    public void javaFunctionCall() {
        String template = "${myBean.customPrint(fruits)}";

        ArrayList<String> fruits = Lists.newArrayList("banana", "orange");

        String res = FreemarkerUtils.processStrTpl(
            template,
            ImmutableMap.of("fruits", fruits, "myBean", new Formatter()));
        System.out.println(res);
    }

    // method in freemarker is a model value which implements TemplateMethodModel interface
    @Test
    public void methodCall() {
        String template = "${join(fruits)}";

        String out = FreemarkerUtils.processStrTpl(
            template,
            ImmutableMap.of(
                "fruits", Lists.newArrayList("banana", "orange"),
                "join", new CustomMethod()));

        Assert.assertEquals("\"banana\",\"orange\"", out);
    }

    public static class Formatter {

        public String customPrint(Collection<Object> coll) {
            StringBuilder sb = new StringBuilder();
            for (Object item : coll) {
                sb.append("\"");
                sb.append(item.toString());
                sb.append("\"");
                sb.append(",");
            }
            return sb.toString();
        }

    }

    public static class CustomMethod implements TemplateMethodModelEx {

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            TemplateSequenceModel sequence = (TemplateSequenceModel) arguments.get(0);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < sequence.size(); i++) {
                items.add(sequence.get(i).toString());
            }
            String output = items.stream()
                .map(x -> "\"" + x + "\"")
                .collect(Collectors.joining(","));
            return output;
        }

    }
    
    

}
