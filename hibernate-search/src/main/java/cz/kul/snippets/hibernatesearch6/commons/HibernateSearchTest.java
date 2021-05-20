package cz.kul.snippets.hibernatesearch6.commons;

import cz.kul.snippets.jpa.common.AbstractJPATest;
import org.junit.Before;
import org.springframework.util.FileSystemUtils;

import java.io.File;

public abstract class HibernateSearchTest extends AbstractJPATest {

    @Before
    public void before() {
        FileSystemUtils.deleteRecursively(new File(getTmpDir()));
    }

    @Override
    public Class<?>[] getConfigClasses() {
        return null;
    }

    public abstract String getTmpDir();

}
