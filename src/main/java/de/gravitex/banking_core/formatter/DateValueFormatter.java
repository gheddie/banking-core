package de.gravitex.banking_core.formatter;

import java.text.DateFormat;
import java.util.Date;

import de.gravitex.banking_core.formatter.base.ValueFormatter;

public class DateValueFormatter extends ValueFormatter {

	private static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);	

	@Override
	public String format(Object aValue) {
		if (aValue == null) {
			return EMPTY_VALUE;
		}
		return dateFormat.format((Date) aValue);
	}
}