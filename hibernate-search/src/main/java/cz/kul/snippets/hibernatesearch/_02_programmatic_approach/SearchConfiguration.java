package cz.kul.snippets.hibernatesearch._02_programmatic_approach;

import org.hibernate.search.cfg.SearchMapping;

import java.lang.annotation.ElementType;

public class SearchConfiguration {

    public static SearchMapping createSearchMapping() {
        SearchMapping sm = new SearchMapping();
//        sm.entity(Person.class).indexed();
//        sm.entity(Person.class).property("wholeName", ElementType.METHOD).field().name("wholeName");


//        sm.entity(AbstractPerson.class).indexed();
        sm.entity(Person.class).indexed();
        sm.entity(AbstractPerson.class).property("wholeName", ElementType.METHOD).field().name("wholeName");


        return sm;
    }

}
