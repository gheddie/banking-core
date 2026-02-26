package de.gravitex.banking_core.formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import de.gravitex.banking_core.formatter.base.ValueFormatter;

public class DateTimeValueFormatter extends ValueFormatter {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");

	@Override
	public String format(Object aValue) {
		if (aValue == null) {
			return EMPTY_VALUE;
		}
		return formatter.format((LocalDate) aValue);
	}
}