package cz.kul.snippets.spring._02_xmlconfig_dependencyinjection;

public class JsonOutputGenerator implements IOutputGenerator {

	@Override
	public String generateOutput() {
		return "Generating JSON output";
	}

}
