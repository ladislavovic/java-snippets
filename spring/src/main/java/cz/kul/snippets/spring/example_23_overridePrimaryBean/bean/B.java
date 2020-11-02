package cz.kul.snippets.spring.example_23_overridePrimaryBean.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class B {

	@Autowired
	private A a;

}
