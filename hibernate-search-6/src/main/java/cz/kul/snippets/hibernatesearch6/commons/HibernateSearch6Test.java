package cz.kul.snippets.hibernatesearch6.commons;

import cz.kul.snippets.jpa.common.AbstractJPATest;
import org.junit.Before;
import org.springframework.util.FileSystemUtils;

import java.io.File;

public abstract class HibernateSearch6Test extends AbstractJPATest {

    @Override
    public Class<?>[] getConfigClasses() {
        return null;
    }

}
