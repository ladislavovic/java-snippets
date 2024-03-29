package cz.kul.snippets.java.service;

public class HelloService implements Service1 {
    
    @Override
    public String greet() {
        return "Hello";
    }
}
