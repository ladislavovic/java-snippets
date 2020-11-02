package cz.kul.snippets.spring.example_23_overridePrimaryBean.bean;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class ASub2 extends A {
}
