package cz.kul.snippets.spring._08_javaconfig.importing;

import static org.junit.Assert.assertEquals;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main_SpringJavaConfigImport {
	
	public static void main(String[] args) {
		propertySourceAndImport();
	}
	
	private static void propertySourceAndImport() {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
			ctx.register(MainConfig.class);
			ctx.refresh();
			
			Bean1 bean = ctx.getBean(Bean1.class);
			assertEquals("foo", bean.getValue());
		}
	}

}
