package cz.kul.weather.wunderground;

import java.net.MalformedURLException;
import java.net.URL;

import cz.kul.weather.Location;
import junit.framework.TestCase;

public class UrlDataFetcherTest extends TestCase {
	
	UrlDataFetcher fetcher;

	protected void setUp() throws Exception {
		super.setUp();
		fetcher = new UrlDataFetcher();
	}

	public void testFetchData() {
		fail("Not yet implemented");
	}

	public void testCreateURL() throws MalformedURLException {
		URL url = fetcher.createURL(Location.PRAGUE, "12345");
		assertEquals("http://api.wunderground.com/api/12345/conditions/q/50.10,14.28.json", url.toString());
	}

}
