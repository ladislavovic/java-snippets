package cz.kul.weather.webservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import cz.kul.weather.Location;
import cz.kul.weather.Weather;
import cz.kul.weather.WeatherService;
import cz.kul.weather.schemas.WeatherServiceRequest;
import cz.kul.weather.schemas.WeatherServiceResponse;
import cz.kul.weather.schemas.WeatherServiceWeatherCreator;
import cz.kul.weather.schemas.WeatherServiceWeather;

@Endpoint
public class WeatherServiceEndpoint {
	
	@Autowired
	WeatherService weatherService;	
	
	@PayloadRoot(namespace = "http://kul.cz/weather/schemas", localPart = "WeatherServiceRequest")
	public @ResponsePayload	WeatherServiceResponse handleWeatherServiceRequest(@RequestPayload WeatherServiceRequest weatherServiceRequest) {
		
		// (1) Prepare locations
		List<Location> locations = new ArrayList<Location>();
		if (weatherServiceRequest.getLocation() != null) {
			for (String loc : weatherServiceRequest.getLocation()) {
				locations.add(Location.valueOf(loc.toUpperCase()));
			}
		}
		
		// (2) Load data
		List<Weather> weathers = weatherService.getActualWeather(locations);
		
		// (3) Create result
		WeatherServiceResponse result = new WeatherServiceResponse();
		result.setWeather(new ArrayList<WeatherServiceWeather>());
		for (Weather weather : weathers) {
			result.getWeather().add(WeatherServiceWeatherCreator.create(weather));
		}
		return result;		
	}
	
}
