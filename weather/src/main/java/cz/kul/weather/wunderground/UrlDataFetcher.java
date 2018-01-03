package cz.kul.weather.wunderground;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

import cz.kul.weather.Location;

public class UrlDataFetcher implements DataFetcher {
	
	private static final Logger log = Logger.getLogger(UrlDataFetcher.class);

	@Override
	public InputStream fetchData(Location location, String key) throws Exception {
		URL url = createURL(location, key);
		InputStream is = url.openStream();
		return is;
	}
	
	public URL createURL(Location location, String key) throws MalformedURLException {
		String urlStr = String.format("http://api.wunderground.com/api/%s/conditions/q/%.2f,%.2f.json", key, location.getLatitude(), location.getLongitude());
		log.debug("creted url: " + urlStr);
		// urlStr = "http://localhost:8080/weather/json/ostrava.json";
		URL url = new URL(urlStr);
		return url;
	}

}
