package cz.kul.snippets.freemarker.example02_callFunction;

import com.google.common.collect.ImmutableMap;
import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.freemarker.common.FreemarkerUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCallFunction extends SnippetsTest {
    
    public static class ModelObject {
        
        public String myMethod() {
            return "val1";
        }
        
        public int sqrt(int a) {
            return a * a;
        }
        
        public String nullReturning() {
            return null;
        }
        
    }
    
    @Test
    public void test() {
        String res = FreemarkerUtils.processStrTpl("${obj.myMethod()}", ImmutableMap.of("obj", new ModelObject()));
        assertEquals("val1", res);
    
        String res2 = FreemarkerUtils.processStrTpl("${obj.sqrt(5)}", ImmutableMap.of("obj", new ModelObject()));
        assertEquals("25", res2);
        
        // be carefull if the method can returns null. Must use "!" at the end.
        assertThrows(
                RuntimeException.class,
                () -> FreemarkerUtils.processStrTpl("${obj.nullReturning()}", ImmutableMap.of("obj", new ModelObject())));
        
        String res3 = FreemarkerUtils.processStrTpl("${obj.nullReturning()!}", ImmutableMap.of("obj", new ModelObject()));
        assertEquals("", res3);
    }
    
}
