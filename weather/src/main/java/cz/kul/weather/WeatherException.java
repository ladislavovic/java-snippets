package cz.kul.weather;

/**
 * Base exceptio for weather module.
 * 
 * @author Ladislav Kulhanek
 */
public class WeatherException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WeatherException() {
		super();
	}

	public WeatherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WeatherException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeatherException(String message) {
		super(message);
	}

	public WeatherException(Throwable cause) {
		super(cause);
	}

}
