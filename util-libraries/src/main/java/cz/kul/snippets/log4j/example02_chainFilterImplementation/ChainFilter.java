package cz.kul.snippets.log4j.example02_chainFilterImplementation;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Filter which contains other filters.
 */
public class ChainFilter extends Filter {

    /**
     * Delay of initialization process in ms.
     */
    private static final int INITIALIZATION_DELAY = 500;

    /**
     * Filter chain
     */
    private List<Filter> filters = Collections.unmodifiableList(new ArrayList<>());

    /**
     * Explicite list of filters for filter chain. It contains
     * comma separated class names.
     */
    private String filterClassNames;

    /**
     * Comma separated list of base packages for component scan.
     */
    private String basePackages = "cz.kul.snippets.log4j.example02_chainFilterImplementation";

    /**
     * True if the filter is initialized already
     */
    private boolean initialized;

    @Override
    public int decide(LoggingEvent event) {
        for (Filter filter : getFilters()) {
            int decision = filter.decide(event);
            if (decision == NEUTRAL) {
                continue;
            } else {
                return decision;
            }
        }
        return NEUTRAL;
    }

    /**
     * It is called when all properties are set by calling setter methods.
     * See {@link OptionHandler#activateOptions()}
     */
    @Override
    public void activateOptions() {
        initializeWithDelay();
    }

    private void initializeWithDelay() {
//        Thread initThread = new Thread(
//                new DelayedInitializer(this, INITIALIZATION_DELAY),
//                ChainFilter.class.getSimpleName() + "_initThread");
//        initThread.start();
        DelayedInitializer delayedInitializer = new DelayedInitializer(this, INITIALIZATION_DELAY);
        delayedInitializer.initWithoutDelay();
    }

    public String getFilterClassNames() {
        return filterClassNames;
    }

    public void setFilterClassNames(String filterClassNames) {
        this.filterClassNames = filterClassNames;
    }

    protected List<Filter> getFilters() {
        return filters;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public boolean isInitialized() {
        return initialized;
    }

    private static class DelayedInitializer implements Runnable {

        private ChainFilter chainFilter;
        
        private int delay;

        public DelayedInitializer(ChainFilter chainFilter, int delay) {
            this.chainFilter = chainFilter;
            this.delay = delay;
        }

        public void initWithoutDelay() {
            try {
                initialize();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(delay); 
                initialize();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        public void initialize() throws ReflectiveOperationException {
            String classNames = chainFilter.getFilterClassNames() != null
                    ? chainFilter.getFilterClassNames()
                    : findFiltersOnClasspath();
            String[] classNamesArr = classNames.split(",");
            List filters = new ArrayList<>(classNamesArr.length);
            for (String className : classNamesArr) {
                Class<?> clazz = Class.forName(className);
                Filter filter = (Filter) clazz.newInstance();
                filters.add(filter);
            }
            chainFilter.filters = filters;
            chainFilter.initialized = true;
        }

        private String findFiltersOnClasspath() {
            ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(Annotation1.class));
            Set<BeanDefinition> components = new HashSet();
            for (String basePackage : chainFilter.getBasePackages().split(",")) {
                components.addAll(scanner.findCandidateComponents(basePackage));
            }
            String classNames = components
                    .stream()
                    .map(x -> x.getBeanClassName())
                    .collect(Collectors.joining(","));
            return classNames;
        }
    }

}
