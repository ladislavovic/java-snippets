package cz.kul.weather.wunderground;

import java.io.InputStream;

import cz.kul.weather.Location;

public class ClasspathDataFetcher implements DataFetcher {

	@Override
	public InputStream fetchData(Location location, String key) throws Exception {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream("ostrava.json");
	}
	
}

