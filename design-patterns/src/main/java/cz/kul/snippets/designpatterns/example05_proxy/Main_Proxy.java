package cz.kul.snippets.designpatterns.example05_proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Description</h1> Proxy wrap real object (or another proxy) and add a functionality.
 * It can chache, protect, log, manage transactions, ... It do something and also can
 * delegate to real object (or another proxy)
 * 
 * <h1>Examples</h1>
 * <ul>
 * <li>beans in spring - transaction handling</li>
 * <li>lazy proxy in hibernate</li>
 * </ul>
 * 
 * <h1>Terms</h1> subject - interface which is implemented by real object and proxy real
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_Proxy {

    public static void main(String[] args) {
        DataSource dataSource = new DataSourceProxy(new DataSourceReal());
        dataSource.getData("1"); // from real data source
        dataSource.getData("1"); // from proxy cache
    }

}

interface DataSource {
    String getData(String key);
}

class DataSourceProxy implements DataSource {

    private Map<String, String> cache = new HashMap<>();

    private DataSource delegated;

    public DataSourceProxy(DataSource delegated) {
        super();
        this.delegated = delegated;
    }

    @Override
    public String getData(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            String data = delegated.getData(key);
            cache.put(key, data);
            return data;
        }
    }
}

class DataSourceReal implements DataSource {

    @Override
    public String getData(String key) {
        if ("1".equals(key)) {
            return "one";
        } else if ("2".equals(key)) {
            return "two";
        } else {
            return null;
        }
    }
}
