package cz.kul.snippets.spring._10_factoryBean;

public class Bean1 {
    
    private String id;
    
    public Bean1() {
    }
    
    public Bean1(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
