package cz.kul.snippets.spring._08_javaconfig.test_autowiring;

import org.springframework.beans.factory.annotation.Autowired;

public class Bean3 {

    @Autowired
    private Bean1 bean1;

    public Bean1 getBean1() {
        return bean1;
    }

}
