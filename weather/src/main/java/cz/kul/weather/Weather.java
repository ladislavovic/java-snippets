package cz.kul.weather;

import java.io.Serializable;
import java.util.Date;

/**
 * Contains information about weather - temperature, wind, ...
 * 
 * @author Ladislav Kulhanek
 */
public class Weather implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Temperature in Celsius */
	private Float tempC;

	/** Relative humidity. It can be value from 0 to 1 */
	private Float relativeHumidity;

	/** Direction of wind in degrees */
	private Integer windDegrees;

	/** Wind speed in m/s */
	private Float windSpeed;

	/** Feels like temperature in Celsius */
	private Float feelslikeTempC;

	/** Location, what is weather for */
	private Location location;

	/** Time of observation */
	private Date observationTime;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Float getTempC() {
		return tempC;
	}
	
	/**
	 * Returns temperarure in fahrenheit.
	 */
	public Float getTempF() {
		if (tempC == null)
			return null;
		return (tempC * 1.8f) + 32;
	}

	public void setTempC(Float tempC) {
		this.tempC = tempC;
	}

	public Float getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(Float relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public Integer getWindDegrees() {
		return windDegrees;
	}

	public void setWindDegrees(Integer windDegrees) {
		this.windDegrees = windDegrees;
	}

	public Float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Float getFeelslikeTempC() {
		return feelslikeTempC;
	}

	public void setFeelslikeTempC(Float feelslikeTempC) {
		this.feelslikeTempC = feelslikeTempC;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}

	@Override
	public String toString() {
		return "Weather [tempC=" + tempC + ", relativeHumidity=" + relativeHumidity + ", windDegrees=" + windDegrees + ", windSpeed=" + windSpeed + ", feelslikeTempC=" + feelslikeTempC + ", location=" + location + ", observationTime="
				+ observationTime + "]";
	}

}
