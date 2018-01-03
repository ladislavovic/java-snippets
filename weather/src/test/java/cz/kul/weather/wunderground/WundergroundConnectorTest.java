package cz.kul.weather.wunderground;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;
import cz.kul.weather.IntegrationUtils;
import cz.kul.weather.Location;
import cz.kul.weather.Weather;

public class WundergroundConnectorTest extends TestCase {

	WundergroundConnector wc;
	
	protected void setUp() throws Exception {
		super.setUp();
		wc = IntegrationUtils.createWundergroundConnector();
	}

	public void testGetActualWeather() throws ParseException {
		Weather weather = wc.getActualWeather(Location.PRAGUE);
		assertEquals(14.9, weather.getTempC(), 0.01);
		assertEquals(0.26, weather.getRelativeHumidity(), 0.01);
		assertEquals(100, weather.getWindDegrees().intValue());
		assertEquals(0.447, weather.getWindSpeed(), 0.01);
		assertEquals(14.9, weather.getFeelslikeTempC(), 0.01);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		assertEquals(sdf.parse("Sat, 23 Nov 2013 02:16:00 -0800"), weather.getObservationTime());
		assertEquals(Location.PRAGUE, weather.getLocation());
	}

	

}
