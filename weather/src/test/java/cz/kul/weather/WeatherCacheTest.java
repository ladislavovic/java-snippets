package cz.kul.weather;

import java.util.Date;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import junit.framework.TestCase;

import static org.mockito.Mockito.*;

public class WeatherCacheTest extends TestCase {

	WeatherCache weatherCache;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		Weather mockedWeather = mock(Weather.class);
				
		Element element = new Element(Location.PRAGUE, mockedWeather);
		Element spyElement = spy(element);
		long beforeTenMinutes = new Date().getTime() - (10 * 60 * 1000);
		when(spyElement.getLastUpdateTime()).thenReturn(beforeTenMinutes);		
		
		Cache mockedCache = mock(Cache.class);
		when(mockedCache.get(Location.PRAGUE)).thenReturn(element);
		
		weatherCache = new WeatherCache();
		weatherCache.setCache(mockedCache);
	}

	public void testGetWeather() {
		assertNull(weatherCache.getWeather(Location.BERLIN, 0));
		assertNull(weatherCache.getWeather(Location.PRAGUE, 5 * 60 * 1000));
		assertNotNull(weatherCache.getWeather(Location.PRAGUE, 0));
		assertNotNull(weatherCache.getWeather(Location.PRAGUE, 20 * 60 * 1000));
	}

	public void testPutWeather() {
		fail("Not yet implemented");
	}

}
