package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InstanceFactory {

    @Bean(name = "instanceFactoryProduct")
    public FactoryProduct getInstance() {
        return new FactoryProduct();
    }

}
