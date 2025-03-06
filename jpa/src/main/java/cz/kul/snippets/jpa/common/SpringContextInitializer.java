package cz.kul.snippets.jpa.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class SpringContextInitializer {

    private final static Logger log = Logger.getLogger(SpringContextInitializer.class);

    private String[] autoscanBasePackages;

    private Class<?>[] configClasses;

    private Map<String, Object> properties;

    public SpringContextInitializer(String[] autoscanBasePackages, Class<?>[] configClasses, Map<String, Object> properties) {
        this.autoscanBasePackages = autoscanBasePackages != null ? autoscanBasePackages : new String[0];
        this.configClasses = configClasses != null ? configClasses : new Class[0];
        this.properties = new HashMap<>();
        this.properties.putAll(properties);
    }

    public AbstractApplicationContext initialize() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        if (ArrayUtils.isNotEmpty(configClasses)) {
            ctx.register(configClasses);
        }
        if (ArrayUtils.isNotEmpty(autoscanBasePackages)) {
            ctx.scan(autoscanBasePackages);
        }
        MapPropertySource springContextInitializerSource = new MapPropertySource("springContextInitializerSource", properties);
        ctx.getEnvironment().getPropertySources().addFirst(springContextInitializerSource);
        log.info("### Context refreshing...");
        ctx.refresh();
        log.info("### Context refreshing END");
        return ctx;
    }
}
