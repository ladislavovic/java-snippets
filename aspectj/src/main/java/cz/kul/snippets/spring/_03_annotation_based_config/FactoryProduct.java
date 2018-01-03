package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FactoryProduct {

    FactoryProduct() {

    }

    @Bean(name = "staticFactoryProduct")
    public static FactoryProduct getInstance() {
        return new FactoryProduct(); // It can be also singleton, you can modify this factory method as you want of course.
    }

}
