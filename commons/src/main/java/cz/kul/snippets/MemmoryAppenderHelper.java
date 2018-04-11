package cz.kul.snippets;

import org.apache.log4j.Logger;

public class MemmoryAppenderHelper {

	public static String DEFAULT_APPENDER_NAME = "MEMMORY_APPENDER";

	public String appenderName;

	public static MemmoryAppenderHelper getInstance() {
		return getInstance(DEFAULT_APPENDER_NAME);
	}

	public static MemmoryAppenderHelper getInstance(String appenderName) {
		return new MemmoryAppenderHelper(appenderName);
	}

	private MemmoryAppenderHelper(String appenderName) {
		this.appenderName = appenderName == null ? DEFAULT_APPENDER_NAME : appenderName;
	}

	public MemmoryAppender getAppender() {
		MemmoryAppender a = (MemmoryAppender) Logger.getRootLogger().getAppender(appenderName);
		return a;
	}

	public void cleanAppender() {
		getAppender().clean();
	}

}
