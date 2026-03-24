package de.gravitex.banking_core.service.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

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

	public static LocalDateRange forActualMonth() {
		return forDates(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()),
				LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public LocalDate getUntilDate() {
		return untilDate;
	}

	public static LocalDateRange around(LocalDate aLocalDate, int aDeviation) {
		return new LocalDateRange(aLocalDate.minusDays(aDeviation), aLocalDate.plusDays(aDeviation));
	}
	
	@Override
	public String toString() {
		return "[" + fromDate + "] - [" + untilDate + "]";
	}

	public boolean contains(LocalDate aLocalDate) {
		boolean begin = aLocalDate.isAfter(fromDate) || aLocalDate.equals(fromDate);
		boolean end = aLocalDate.isBefore(untilDate) || aLocalDate.equals(untilDate);
		return begin && end;
	}

	public List<LocalDate> getDays() {
		List<LocalDate> days = new ArrayList<>();
		LocalDate actualDate = fromDate;
		while (actualDate.isBefore(untilDate) || actualDate.equals(untilDate)) {
			days.add(actualDate);
			actualDate = actualDate.plusDays(1);
		}
		return days;
	}
}