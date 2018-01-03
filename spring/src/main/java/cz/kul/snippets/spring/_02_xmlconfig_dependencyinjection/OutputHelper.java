package cz.kul.snippets.spring._02_xmlconfig_dependencyinjection;

public class OutputHelper {
	
	private IOutputGenerator outputGenerator;
	
	public OutputHelper() {
		// Default constructor must be present
		// due to setter dependency injection. First
		// it creates class by default constructor and
		// then use setter for the injection.
	}

	public OutputHelper(IOutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
	}
	
	public void generateOutput() {
		System.out.println(outputGenerator.generateOutput());
	}

	public IOutputGenerator getOutputGenerator() {
		return outputGenerator;
	}

	public void setOutputGenerator(IOutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
	}
	
}
