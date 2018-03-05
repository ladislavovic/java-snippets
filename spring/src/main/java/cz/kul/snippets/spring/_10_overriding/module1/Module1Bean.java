package cz.kul.snippets.spring._10_overriding.module1;

import org.springframework.stereotype.Component;

@Component("foo")
public class Module1Bean {

    private String val;

    public Module1Bean() {
    }

    public Module1Bean(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
