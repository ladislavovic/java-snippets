package cz.kul.snippets.jpa.example01_modifyEntityDuringLoadByEventListener;

import cz.kul.snippets.jpa.common.model.Person;
import org.hibernate.event.spi.*;

public class PersonNameModifier implements PostLoadEventListener {

    public static String GENERATED_PERSON_NAME = "generated_person_name";

    @Override
    public void onPostLoad(PostLoadEvent event) {
        if (event.getEntity() instanceof Person) {
            Person person = (Person) event.getEntity();
            person.setName(GENERATED_PERSON_NAME);
        }
    }

}
