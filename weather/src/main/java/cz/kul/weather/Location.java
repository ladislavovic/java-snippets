package cz.kul.weather;

import java.io.Serializable;

/**
 * Geographical location.
 * 
 * @author Ladislav Kulhanek
 */
public enum Location implements Serializable {
	
	OSTRAVA(49.6800f, 18.1200f),
	PRAGUE(50.0999f, 14.2799f),
	BERLIN(52.5241f, 13.4559f),
	LONDON(51.4799f, -0.4499f);	

	private float latitude;
	
	private float longitude;
	
	private Location(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}
		
}
