package cz.kul.snippets.guava.example03_cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import cz.kul.snippets.SnippetsTest;
import org.junit.Assert;
import org.junit.Test;

public class TestCache extends SnippetsTest {

    // Cache loading is preferred over cache.put()
    @Test
    public void loadingCache() {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .build(
                        new CacheLoader<Integer, String>() {
                            public String load(Integer key) {
                                return key.toString();
                            }
                        }
                );
        Assert.assertEquals("10", cache.getUnchecked(10));
    }
    
    @Test
    public void notLoadingCache() throws Exception{
        Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
        Assert.assertEquals(null, cache.getIfPresent(10));
        cache.put(10, "10");
        Assert.assertEquals("10", cache.getIfPresent(10));
        Assert.assertEquals("20", cache.get(20, () -> "20"));
    }
    
    @Test
    public void sizeBasedEviction() {
        Cache<Integer, String> cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .build();
        cache.put(1, "1");
        cache.put(2, "2");
        cache.put(3, "3");
        Assert.assertTrue(cache.asMap().keySet().size() <= 2);
    }

    @Test
    public void nullCanNotBeTheKey() {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().build();
        assertThrows(NullPointerException.class, () -> cache.put(null, "null"));
    }
    
}
