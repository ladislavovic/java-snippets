package cz.kul.weather.wunderground;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import cz.kul.weather.Location;
import cz.kul.weather.Weather;
import cz.kul.weather.WeatherConnector;
import cz.kul.weather.WeatherException;

/**
 * Connector, which can get weather from wunderground.com. 
 * 
 * @author Ladislav Kulhanek
 */
public class WundergroundConnector implements WeatherConnector {

	private static final Logger log = Logger.getLogger(WundergroundConnector.class);
	
	/** Key to wunderground API.*/
	private String wundergroundKey;
	
	/** Fetch data from source */
	private DataFetcher dataFetcher;

	public Weather getActualWeather(Location location) {
		InputStream is = null;
		try {
			//
			// (1) Load data
			//
			is = dataFetcher.fetchData(location, wundergroundKey);
			HashMap<String, Object> data = new ObjectMapper().readValue(is, HashMap.class);

			//
			// (2) read values
			//
			Weather result = new Weather();
			result.setLocation(location);
			Map<String, Object> currentObservation = (Map<String, Object>) data.get("current_observation");

			// (2.1) read tempC
			try {
				Double tempC = ((Number) currentObservation.get("temp_c")).doubleValue();
				result.setTempC(tempC.floatValue());
			} catch (Exception e) {
				log.error("An error while reading tempC value. Data: " + data, e);
			}

			// (2.2) read relativeHumidity
			try {
				String relativeHumidity = (String) currentObservation.get("relative_humidity");
				String relativeHumidityNum = null;
				Pattern p = Pattern.compile("([0-9]+)%");
				Matcher m = p.matcher(relativeHumidity);
				if (m.matches()) {
					relativeHumidityNum = m.group(1);
				} else {
					throw new RuntimeException("Can not determine relative humidity.");
				}
				result.setRelativeHumidity(Float.valueOf(relativeHumidityNum) / 100);
			} catch (Exception e) {
				log.error("An error while reading relativeHumidity value. Data: " + data, e);
			}

			// (2.3) windDegrees
			try {
				Integer windDegrees = (Integer) currentObservation.get("wind_degrees");
				if (windDegrees == null) throw new RuntimeException("widn_degrees not found");
				result.setWindDegrees(windDegrees);
			} catch (Exception e) {
				log.error("An error while reading windDegrees value. Data: " + data, e);
			}

			// (2.4) windSpeed
			try {
				Double windSpeed = ((Number) currentObservation.get("wind_mph")).doubleValue();
				windSpeed = windSpeed * 0.44704; // conversion from mph to ms 
				result.setWindSpeed(windSpeed.floatValue());
			} catch (Exception e) {
				log.error("An error while reading windSpeed value. Data: " + data, e);
			}

			// (2.5) feels like temperature
			try {
				String feelsLike = (String) currentObservation.get("feelslike_c");
				if (feelsLike == null) throw new RuntimeException("feelslike_c not found");
				result.setFeelslikeTempC(Float.valueOf(feelsLike));
			} catch (Exception e) {
				log.error("An error while reading feelslike value. Data: " + data, e);
			}

			// (2.5) observation time
			try {
				String observationTime = (String) currentObservation.get("observation_time_rfc822");
				if (observationTime == null) throw new RuntimeException("observatiion_time_rfc822 not found.");
				SimpleDateFormat rfs822 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
				Date date = rfs822.parse(observationTime);
				result.setObservationTime(date);
			} catch (Exception e) {
				log.error("An error while reading observation time value. Data: " + data, e);
			}

			//
			// (3) Returns result
			//
			return result;
		} catch (Exception e) {
			throw new WeatherException("An error while getting weather.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}
	
	public DataFetcher getDataFetcher() {
		return dataFetcher;
	}

	public void setDataFetcher(DataFetcher dataFetcher) {
		this.dataFetcher = dataFetcher;
	}

	public String getWundergroundKey() {
		return wundergroundKey;
	}

	public void setWundergroundKey(String wundergroundKey) {
		this.wundergroundKey = wundergroundKey;
	}

	public static void main(String[] args) {
		WundergroundConnector wc = new WundergroundConnector();
		wc.getActualWeather(null);
	}

}
