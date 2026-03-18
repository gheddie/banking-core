package de.gravitex.banking_core.service.util;

import java.time.LocalDate;

public class ShortestBookingInterval {

	private LocalDate from;

	private LocalDate to;

	private long daySpan;

	public ShortestBookingInterval(long aDaySpan, LocalDate aFrom, LocalDate aTo) {

		super();

		this.daySpan = aDaySpan;
		this.from = aFrom;
		this.to = aTo;
	}

	public long getDaySpan() {
		return daySpan;
	}

	@Override
	public String toString() {
		return "(" + from + "-" + to + ") --> " + daySpan + " days";
	}
}