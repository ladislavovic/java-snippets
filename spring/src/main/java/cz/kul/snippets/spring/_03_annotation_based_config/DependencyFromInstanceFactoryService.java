package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DependencyFromInstanceFactoryService {

    @Autowired
    @Qualifier("instanceFactoryProduct")
    private FactoryProduct dependency;

    public FactoryProduct getDependency() {
        return dependency;
    }

}
