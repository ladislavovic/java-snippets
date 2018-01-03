package cz.kul.snippets.spring._02_xmlconfig_dependencyinjection;

public class CsvOutputGenerator implements IOutputGenerator {

	@Override
	public String generateOutput() {
		return "Generating CVS output";
	}

}

