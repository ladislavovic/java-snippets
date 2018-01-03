package cz.kul.snippets.hibernate._01_hsql;

import java.util.List;

public interface PersonService {
    
    List<Person> getAll();
    
    Person insertPerson(String name);
    
    PersonDetail insertPersonDetail(Person person, String key, String value);

    public void criteriaTest();
    
}
