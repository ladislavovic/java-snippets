package cz.kul.weather.webservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.kul.weather.Location;
import cz.kul.weather.Weather;
import cz.kul.weather.WeatherService;
import cz.kul.weather.schemas.WeatherServiceResponse;
import cz.kul.weather.schemas.WeatherServiceWeather;
import cz.kul.weather.schemas.WeatherServiceWeatherCreator;

@Controller
public class WeatherServiceEndpointRest {

	@Autowired
	WeatherService weatherService;

	@RequestMapping(method = RequestMethod.GET, value = "/weather")
	public @ResponseBody WeatherServiceResponse getWeather() {
		return getWeatherCore(null);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/weather/{location}")
	public @ResponseBody WeatherServiceResponse getWeather(@PathVariable String location) {
		return getWeatherCore(location);
	}

	private WeatherServiceResponse getWeatherCore(String location) {
		// (1) Prepare locations
		List<Location> locations = new ArrayList<Location>();
		if (location != null && !location.isEmpty()) {
			locations.add(Location.valueOf(location.toUpperCase()));
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
