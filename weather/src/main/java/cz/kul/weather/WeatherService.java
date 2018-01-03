package cz.kul.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Service provide weather information. 
 * 
 * @author Ladislav Kulhanek
 */
public class WeatherService {

	private static final Logger log = Logger.getLogger(WeatherService.class);

	/** Max age of actual weather unless background service is down. */
	private long maxActualWeatherAge;

	private WeatherConnector weatherConnector;

	private WeatherCache weatherCache;
	
	public WeatherService(WeatherConnector weatherConnector, WeatherCache weatherCache) {
		this.weatherConnector = weatherConnector;
		this.weatherCache = weatherCache;
	}

	public List<Weather> getActualWeather(List<Location> locations) {
		try {
			if (locations == null || locations.isEmpty()) {
				locations = Arrays.asList(Location.values());
			}
			List<Weather> result = new ArrayList<Weather>();
			for (Location location : locations) {
				try {
					Weather w = getActualWeather(location);
					result.add(w);
				} catch (Exception e) {
					log.error("An errow while getting weather from location " + location);
				}
			}
			return result;			
		} catch (Exception e) {
			String err = "An error while getting actual weather. Locations: " + locations;
			log.error(err, e);
			throw new WeatherException(err, e);
		}
		
	}
	
	/**
	 * Get actual weather from location. Max age of result is smaller
	 * then maxActualWeatherAge unless backgound service is down.
	 * 
	 * @param location Location of weather
	 * @return Weather
	 */
	public Weather getActualWeather(Location location) {
		try {
			// (1) Init
			log.info("Getting weather from location " + location);
			if (location == null) {
				throw new IllegalArgumentException("Location can not be null.");
			}
			Weather result = null;
			
			// (2) Try to return actual weather from cache
			if (result == null) {
				result = weatherCache.getWeather(location, maxActualWeatherAge);
				if (result != null)
					log.debug("Weather returned from cache.");
			}

			// (3) Try to return from background service
			if (result == null) {
				try {
					result = weatherConnector.getActualWeather(location);
					if (result != null) {
						weatherCache.putWeather(result);
					} else {
						throw new RuntimeException();
					}
				} catch (Exception e) {
					log.error("Can not load actual weather from server. Location: " + location);
				}
			}

			// (4) Try to return weather older then maxActualWeatherAge from cache
			if (result == null) {
				result = weatherCache.getWeather(location, 0);
			}
			
			// (5) Return result
			if (result != null) {
				return result;
			} else {			
				// Can not get weather from any source, throw an exception
				throw new WeatherException("Can not get weather. All sources are unavailable.");
			}
		} catch (Exception e) {
			String err = "An error while getting actual weather.";
			log.error(err, e);
			throw new WeatherException(err, e);
		}
	}

	public long getMaxActualWeatherAge() {
		return maxActualWeatherAge;
	}

	public void setMaxActualWeatherAge(long maxActualWeatherAge) {
		this.maxActualWeatherAge = maxActualWeatherAge;
	}

}
