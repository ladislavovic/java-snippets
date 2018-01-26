package cz.kul.snippets.spring._11_parentcontext;

import static org.junit.Assert.assertEquals;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import cz.kul.snippets.spring.common.Bean1;


public class Main_ParentContext {
	
	public static void main(String[] args) {
		beanFromParent();
		propertyFromParent();
	}
	
	private static void beanFromParent() {
		try (Contexts contexts = createParentChildContexts(ParentCfg.class, ChildCfg.class)) {
			Bean1 bean = contexts.child.getBean(Bean1.class);
			assertEquals("parent", bean.getVal());
		}
	}

	private static void propertyFromParent() {
		// NOTE:
		// You can use property source from parent, but if you want
		// to use it in @Value, you must define
		// PropertySourcesPlaceholderConfigurer in child context
		try (Contexts contexts = createParentChildContexts(ParentCfg2.class, ChildCfg2.class)) {
			Bean1 bean = contexts.child.getBean(Bean1.class);
			assertEquals("value from property file", bean.getVal());
		}
	}
	
	private static Contexts createParentChildContexts(Class<?> parentCfg, Class<?> childCfg) {
		AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
		parent.register(parentCfg);
		parent.refresh();
		AnnotationConfigApplicationContext child = new AnnotationConfigApplicationContext();
		child.register(childCfg);
		child.setParent(parent);
		child.refresh();
		Contexts contexts = new Contexts();
		contexts.parent = parent;
		contexts.child = child;
		return contexts;
	}
	
	static class Contexts implements AutoCloseable {
		GenericApplicationContext parent;
		GenericApplicationContext child;
		
		@Override
		public void close() {
			try {
				if (parent != null) {
					parent.close();
				}
			} finally {
				if (child != null) {
					child.close();
				}
			}
		}
	}
	
}
