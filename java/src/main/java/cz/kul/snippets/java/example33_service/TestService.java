package cz.kul.snippets.java.example33_service;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ServiceLoader;

public class TestService {

    /**
     * The whole picture:
     * 
     * 1. There is an interface Service1. It is a Service.
     * 
     * 2. There are two implementations: HiService and HelloService
     * 
     * 3. There is a file in META-INF/services which has the same name as the service. It contains
     *    all implementations available in the module. Each on its own line.
     *    
     * 4. ServiceLoader can on demand find all implementations of the given service, which are
     *    available. 
     */
    @Test
    public void testService1() {
        ServiceLoader<Service1> serviceLoader = ServiceLoader.load(Service1.class);
        ImmutableList<Service1> services = ImmutableList.copyOf(serviceLoader.iterator());
        
        Assert.assertEquals(2, services.size());

        Service1 hiService = services.stream().filter(x -> x instanceof HiService).findFirst().get();
        Assert.assertEquals("Hi", hiService.greet());
        
        Service1 helloService = services.stream().filter(x -> x instanceof HelloService).findFirst().get();
        Assert.assertEquals("Hello", helloService.greet());
    }
    
}
