package cz.kul.snippets.spring.common;

public class Bean1 {
	
	private String val;

	public Bean1() { }

	public Bean1(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return "Bean1 [val=" + val + "]";
	}
	
}
