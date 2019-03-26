package cz.kul.snippets.java.example25_TODO.mxbeanwithcomplextype;

import java.io.Serializable;

public class Bean2ComplexValue implements Serializable {
    
    private String val1;
    private String val2;

    public Bean2ComplexValue(String val1, String val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public String getVal1() {
        return val1;
    }

    public String getVal2() {
        return val2;
    }
    
}
