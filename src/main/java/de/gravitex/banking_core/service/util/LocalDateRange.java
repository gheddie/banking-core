package de.gravitex.banking_core.service.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class LocalDateRange {

	private LocalDate fromDate;

	private LocalDate untilDate;

	private LocalDateRange(LocalDate aFromDate, LocalDate aUntilDate) {

		super();

		this.fromDate = aFromDate;
		this.untilDate = aUntilDate;
	}

	public static LocalDateRange forDates(LocalDate aFromDate, LocalDate aUntilDate) {
		if (aFromDate == null || aUntilDate == null) {
			return forActualMonth();
		}
		if (!aUntilDate.isAfter(aFromDate)) {
			throw new IllegalArgumentException("invalid range [" + aFromDate + " - " + aUntilDate + "]!!!");
		}
		return new LocalDateRange(aFromDate, aUntilDate);
	}

	private static LocalDateRange forActualMonth() {		
		return forDates(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
				LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
	}
	
	public LocalDate getFromDate() {
		return fromDate;
	}
	
	public LocalDate getUntilDate() {
		return untilDate;
	}
}