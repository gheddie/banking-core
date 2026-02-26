package de.gravitex.banking_core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtil {

	public static long getMilliSeconds(LocalDate aLocalDate) {
		return aLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static LocalDate getLocalDate(Long aMilliSeconds) {
		return Instant.ofEpochMilli(aMilliSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
	}
}