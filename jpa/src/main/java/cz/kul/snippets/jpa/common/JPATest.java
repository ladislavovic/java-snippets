package cz.kul.snippets.jpa.common;

import cz.kul.snippets.jpa.common.model.Person;

import java.util.Random;

public abstract class JPATest extends AbstractJPATest {

    @Override
    public String getJpaModelPackages() {
        String commonJPAModel = Person.class.getPackage().getName();
        String testJPAModel = getTestPackageName();
        String JPAModel = commonJPAModel + "," + testJPAModel;
        return JPAModel;
    }

    public long getRandomId() {
        int MAX_VAL = 9999;
        Random random = new Random();
        return random.nextInt(MAX_VAL + 1);
    }

}
