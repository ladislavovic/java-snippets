package cz.kul.snippets.java.example33_service;

public class HelloService implements Service1 {
    
    @Override
    public String greet() {
        return "Hello";
    }
}
