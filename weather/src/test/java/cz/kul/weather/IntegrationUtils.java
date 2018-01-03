package cz.kul.weather;

import cz.kul.weather.wunderground.ClasspathDataFetcher;
import cz.kul.weather.wunderground.WundergroundConnector;

public class IntegrationUtils {
	
	public static WundergroundConnector createWundergroundConnector() {
		WundergroundConnector wc = new WundergroundConnector();
		wc.setWundergroundKey("12345");
		wc.setDataFetcher(new ClasspathDataFetcher());
		return wc;
	}
	
	public static WeatherService createWeatherService() {
		WeatherService ws = new WeatherService(createWundergroundConnector(), new WeatherCache());
		ws.setMaxActualWeatherAge(3600 * 1000 * 3);
		return ws;
	}

}
