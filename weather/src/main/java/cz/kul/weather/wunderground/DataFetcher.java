package cz.kul.weather.wunderground;

import java.io.InputStream;

import cz.kul.weather.Location;

public interface DataFetcher {
	
	InputStream fetchData(Location location, String key) throws Exception;

}
