package cz.kul.snippets;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TomcatBuilder {

    public static int DEFAULT_PORT = 8082;

    private Tomcat tomcat;

    private List<Context> contexts = new ArrayList<>();

    boolean built;

    public TomcatBuilder() {
        this.tomcat = new Tomcat();
        tomcat.setPort(DEFAULT_PORT);
    }

    public TomcatBuilder setPort(int port) {
        tomcat.setPort(port);
        return this;
    }

    public TomcatBuilder addContext() {
        File docBase = new FilesystemHelper().createRandomDir();
        Context ctx = tomcat.addContext("/ctx1", docBase.getAbsolutePath());
        contexts.add(ctx);
        return this;
    }

    public Suite build() {
        assertNotBuilt();
        built = true;
        Suite suite = new Suite(tomcat, getContext(0), contexts);
        return suite;
    }

    public boolean isBuilt() {
        return built;
    }

    private Context getContext(int i) {
        return contexts.size() <= i ? null : contexts.get(i);
    }

    private void assertBuilt() {
        if (isBuilt()) return;
        throw new IllegalStateException("Tomcat is not built yet");
    }

    private void assertNotBuilt() {
        if (!isBuilt()) return;
        throw new IllegalStateException("Tomcat is built already");
    }

    public static class Suite {
        public Tomcat tomcat;
        public Context ctx;
        public List<Context> allCtxs;

        public Suite(Tomcat tomcat, Context ctx, List<Context> allCtxs) {
            this.tomcat = tomcat;
            this.ctx = ctx;
            this.allCtxs = allCtxs;
        }
    }

}
