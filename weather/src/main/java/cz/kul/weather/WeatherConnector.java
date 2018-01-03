package cz.kul.weather;

/**
 * Can connect to backgroud weather service.
 * 
 * @author Ladislav Kulhanek
 */
public interface WeatherConnector {
	
	Weather getActualWeather(Location location);

}
