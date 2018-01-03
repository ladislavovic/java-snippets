package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrimaryInjected {

    private Baz baz;

    @Autowired
    public PrimaryInjected(Baz baz) {
        super();
        this.baz = baz;
    }

    public Baz getBaz() {
        return baz;
    }

}
