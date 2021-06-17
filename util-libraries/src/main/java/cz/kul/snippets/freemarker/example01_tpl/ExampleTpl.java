package cz.kul.snippets.freemarker.example01_tpl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExampleTpl {

    @Test
    public void binding() {
        String res = FreemarkerUtils.process("Hello world, ${user}!", ImmutableMap.of("user", "Monica"));
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

        String res = FreemarkerUtils.process(template, ImmutableMap.of("fruits", fruits));
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
            String out = FreemarkerUtils.process("<#list map?keys as key>${key} </#list>", dataModel);
            Assert.assertEquals("simpleVal listVal mapVal ", out);
        }

        {
            // iterate values
            String out = FreemarkerUtils.process("<#list map.mapVal?values as val>${val} </#list>", dataModel);
            Assert.assertEquals("banana ", out);
        }

        {
            // get particular value - static
            String out = FreemarkerUtils.process("<#list map.listVal as val>${val} </#list>", dataModel);
            Assert.assertEquals("banana orange ", out);
        }

        {
            // get particular value - dynamic
            String out = FreemarkerUtils.process("<#list map.mapVal?keys as key>${key}=${map.mapVal[key]}</#list>", dataModel);
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

        String res = FreemarkerUtils.process(template, ImmutableMap.of("fruits", fruits));
        assertEquals("", res);
    }

    @Test
    public void nullValueCauseError() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("user", null);
        try {
            FreemarkerUtils.process("${user}", model);
            Assert.fail("exception expected");
        } catch (Exception e) {
        }

        String res = FreemarkerUtils.process("${user!\"anonymous\"}", model);
        assertEquals("anonymous", res);
    }
    
    
    @Test
    public void missingValueCauseError() {
        try {
            FreemarkerUtils.process("${user}!", Maps.newHashMap());
            Assert.fail("exception expected");
        } catch (Exception e) {
        }

        String res = FreemarkerUtils.process("${user!\"anonymous\"}", Maps.newHashMap());
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

        String res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("monica,MONICA,Monica,6", res);
    }
    
    @Test
    public void testMissingValues() {
        String tpl = "<#if user??>${user}</#if>";
        
        String res = FreemarkerUtils.process(tpl, new HashMap<>());
        assertEquals("", res);
        
        res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("monica", res);
    }

    @Test
    public void testEmptyValue() {
        String tpl = "<#if user?has_content>A${user}A</#if>";

        String res = FreemarkerUtils.process(tpl, new HashMap<>());
        assertEquals("", res);

        res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", ""));
        assertEquals("", res);

        res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("AmonicaA", res);
    }
    
    @Test
    public void setCanBeAlsoAccessedByIndex() {
        String tpl = "${names[0]}";
        
        String res = FreemarkerUtils.process(tpl, ImmutableMap.of("names", Sets.newHashSet("monica", "rachel")));
        assertTrue("monica".equals(res) || "rachel".equals(res));
    }
    
    @Test
    public void unassign() {
        String tpl = "<#assign foo=true><#if foo>true</#if>";
        
        String res = FreemarkerUtils.process(tpl, new Object());
        assertEquals("true", res);
    }

    @Test
    public void stringComparison() {
        String tpl = "<#if user == \"monica\" || user == \"rachel\">match</#if>";

        String res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", ""));
        assertEquals("", res);

        res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", "monica"));
        assertEquals("match", res);
        
        res = FreemarkerUtils.process(tpl, ImmutableMap.of("user", "rachel"));
        assertEquals("match", res);
    }

    @Test
    public void numberFormat() {
        String tpl = "${num}";

        String res = FreemarkerUtils.process(tpl, ImmutableMap.of("num", 1000));
        assertEquals("1000", res);
    }
    
    

}
