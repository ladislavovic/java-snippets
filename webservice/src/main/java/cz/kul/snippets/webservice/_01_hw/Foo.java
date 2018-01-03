package cz.kul.snippets.webservice._01_hw;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Foo {
	
	private static final Logger log = Logger.getLogger(Foo.class);

	public Foo() {
		log.debug("AAAA");
		File f = new File("/tmp/log/bar.txt");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
