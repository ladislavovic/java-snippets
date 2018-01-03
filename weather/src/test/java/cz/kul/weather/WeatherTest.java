package cz.kul.weather;

import junit.framework.TestCase;

public class WeatherTest extends TestCase {

	private Weather weather;
	
	protected void setUp() throws Exception {
		super.setUp();
		weather = new Weather();
		weather.setTempC(10f);
	}

	public void testGetTempF() {
		assertEquals(50f, weather.getTempF(), 0.1f);
	}

}
