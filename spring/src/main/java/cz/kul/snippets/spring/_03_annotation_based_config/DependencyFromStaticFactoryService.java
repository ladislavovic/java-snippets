package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DependencyFromStaticFactoryService {

    @Autowired
    @Qualifier("staticFactoryProduct")
    FactoryProduct injected;

    public FactoryProduct getInjected() {
        return injected;
    }

}
