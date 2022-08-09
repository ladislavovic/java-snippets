package cz.kul.snippets.spring.example_26_configFileReloading;

import cz.kul.snippets.ThreadUtils;
import cz.kul.snippets.spring.common.SpringTestUtils;

public class Application {

	public static void main(String[] args) {
		SpringTestUtils.runInSpring(ContextConfiguration.class, ctx -> {
			PetsCfg bean = ctx.getBean(PetsCfg.class);
			System.out.println(bean);
			ThreadUtils.sleep(100_000);
		});
	}

}
