package cz.kul.snippets.jpa.common;

import cz.kul.snippets.jpa.common.model.Person;

public abstract class JPATest extends AbstractJPATest {

    @Override
    public String getJpaModelPackages() {
        String commonJPAModel = Person.class.getPackage().getName();
        String testJPAModel = getTestPackageName();
        String JPAModel = commonJPAModel + "," + testJPAModel;
        return JPAModel;
    }

}
