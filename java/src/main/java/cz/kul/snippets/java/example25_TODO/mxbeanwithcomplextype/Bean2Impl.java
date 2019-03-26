package cz.kul.snippets.java.example25_TODO.mxbeanwithcomplextype;

public class Bean2Impl implements Bean2MXBean {
    
    @Override
    public Bean2ComplexValue getValue() {
        return new Bean2ComplexValue("foo", "bar");
    }
    
}
