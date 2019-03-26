package cz.kul.snippets.java.example25_TODO.mbeanwithcomplextype;

public class Bean1 implements Bean1MBean {
    
    @Override
    public Bean1ComplexValue getValue() {
        return new Bean1ComplexValue("foo", "bar");
    }
    
}
