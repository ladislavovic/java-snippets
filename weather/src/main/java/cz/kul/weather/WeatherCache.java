package cz.kul.weather;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache Weather objects. Cache is persistent and data in cache
 * livs forever. Caching key is Location.
 * 
 * @author Ladislav Kulhanek
 */
public class WeatherCache {
	
	private Cache cache;
	
	public WeatherCache() {
		CacheManager cm = CacheManager.create(Thread.currentThread().getContextClassLoader().getResourceAsStream("ehcache.xml"));
		cache = cm.getCache("weatherCache");
	}

	/**
	 * Returns Weather object from cache. Object has to be younger
	 * then maxAge. If the object is in the cache, but it is older,
	 * it returns null.
	 *  
	 * @param location Location of weather
	 * @param maxAge Max available age of instance. If it is 0, then
	 *   maxAge is ignored and object is returned regardless of age.
	 * @return Weather object
	 */
	public Weather getWeather(Location location, long maxAge) {
		Element element = cache.get(location);
		if (element == null)
			return null;
		Weather result = (Weather) element.getObjectValue();
		
		if (maxAge == 0)
			return result;
		
		Long time = element.getLastUpdateTime();
		if (time == null) {
			time = element.getCreationTime();
		}
		if (System.currentTimeMillis() - time < maxAge) {
			return result;
		}
		
		return null;	
	}
	
	/** 
	 * Put weather instance to cache.
	 * @param location
	 * @param weather
	 */
	public void putWeather(Weather weather) {
		Element el = new Element(weather.getLocation(), weather);
		cache.put(el);
	}

	protected Cache getCache() {
		return cache;
	}

	protected void setCache(Cache cache) {
		this.cache = cache;
	}
	
}
