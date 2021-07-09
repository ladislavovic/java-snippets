package cz.kul.snippets.spring.example_24_spel;

public class ForbiddenClass {

	public static String staticDangerousOperation() {
		return "doing something nasty";
	}

	public String dangerousOperation() {
		return "doing something nasty";
	}

}
