package cz.kul.snippets.spring._08_javaconfig;

public class Bean2 {

	private Bean1 bean1;
	
	public Bean2(Bean1 bean1) {
		this.bean1 = bean1;
	}

	public Bean1 getBean1() {
		return bean1;
	}

	public void setBean1(Bean1 bean1) {
		this.bean1 = bean1;
	}
	
}
