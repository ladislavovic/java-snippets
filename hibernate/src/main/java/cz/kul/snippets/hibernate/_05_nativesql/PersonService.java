package cz.kul.snippets.hibernate._05_nativesql;

import java.util.List;

public interface PersonService {
    
    List<Person5> getAll();
    
    Person5 insertPerson(String name);
    
    PersonDetail5 insertPersonDetail(Person5 person, String key, String value);
    
    void testIt();
    
}
