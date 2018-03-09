package cz.kul.snippets.spring._08_javaconfig.test_autowiring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bean2 {

    @Autowired
    private Bean1 bean1;

    public Bean1 getBean1() {
        return bean1;
    }

}
