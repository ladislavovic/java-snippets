package cz.kul.weather;

import junit.framework.TestCase;

public class WeatherServiceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetActualWeather() {
		WeatherService ws = IntegrationUtils.createWeatherService();
		Weather w = ws.getActualWeather(Location.PRAGUE);
		assertNotNull(w);
		assertEquals(14.9f, w.getTempC(), 0.01);

		try {
			ws.getActualWeather((Location)null);
			fail("Exception was expected.");
		} catch (WeatherException e) {
		}
	}

}
