package cz.kul.snippets.freemarker.example03_enum;

import com.google.common.collect.ImmutableMap;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEnum {
    
    public enum Color { RED, GREEN, BLUE }
    
    public static class ModelObj {
        
        public Color getColor() {
            return Color.RED;
        }
        
        public String functionWithEnumParam(Color c) {
            return c.name();
        }
        
    }

    @Test
    public void test() {
        {
            String res = FreemarkerUtils.process("${obj.color}", ImmutableMap.of("obj", new ModelObj()));
            assertEquals("RED", res);
        }
        
        {
            String res = FreemarkerUtils.process("${(obj.color.name() == \"RED\")?string}", ImmutableMap.of("obj", new ModelObj()));
            assertEquals("true", res);
        }
        
    }
    
}
