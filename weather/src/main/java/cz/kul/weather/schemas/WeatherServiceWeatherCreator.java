package cz.kul.weather.schemas;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import cz.kul.weather.Weather;

public class WeatherServiceWeatherCreator {
	
	private static final Logger log = Logger.getLogger(WeatherServiceWeatherCreator.class);
	
	public static WeatherServiceWeather create(Weather weather) {
		WeatherServiceWeather result = new WeatherServiceWeather();
		result.setTempC(weather.getTempC());
		result.setFeelslikeTempC(weather.getFeelslikeTempC());
		result.setRelativeHumididy(weather.getRelativeHumidity());
		result.setWindDegrees(weather.getWindDegrees());
		result.setWindSpeed(weather.getWindSpeed());
		result.setLocation(weather.getLocation().name());
		result.setLocationLangitude(weather.getLocation().getLatitude());
		result.setLocationLongitude(weather.getLocation().getLongitude());
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(weather.getObservationTime());		
		try {
			result.setObservationTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
		} catch (DatatypeConfigurationException e) {
			log.error(e);
		}
		return result;
	}

}
