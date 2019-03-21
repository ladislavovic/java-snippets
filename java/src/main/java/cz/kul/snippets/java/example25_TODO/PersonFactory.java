package cz.kul.snippets.java.example25_TODO;

public class PersonFactory implements PersonFactoryMBean {
    
    private int counter;
    
    public Person createPerson(String name) {
        counter++;
        return new Person(name);
    }

    @Override
    public int getNumberOfPersons() {
        return counter;
    }

    @Override
    public void resetNumberOfPersons() {
        counter = 0;
    }
    
}
