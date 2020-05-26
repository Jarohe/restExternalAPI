package com.example.restservice.util;

import java.util.concurrent.TimeUnit;

public class Util {
	public static String getFormatElapsedTime(long milliseconds) {
		return String.format("%02d:%02d:%02d,", TimeUnit.MILLISECONDS.toHours(milliseconds),
				TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}

}
