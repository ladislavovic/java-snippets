package _01_servlet_request_listener;

import cz.kul.snippets.AgentServlet;
import cz.kul.snippets.TomcatBuilder;
import cz.kul.snippets.TomcatTestCase;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.junit.Test;

import java.io.File;

public class TestServletRequestListener extends TomcatTestCase {

    @Test
    public void test1() throws LifecycleException {
        TomcatBuilder.Suite suite = new TomcatBuilder()
                .addContext()
                .build();

        suite.tomcat.addSe

        suite.ctx.getServletContext().addServlet("agent", AgentServlet.class);
        suite.ctx.addServletMappingDecoded("/*", "agent");

        suite.tomcat.start();
        suite.tomcat.getServer().await();

    }

}
