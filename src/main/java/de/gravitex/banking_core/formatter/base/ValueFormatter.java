package de.gravitex.banking_core.formatter.base;

public abstract class ValueFormatter {
	
	protected static final String EMPTY_VALUE = "";

	public abstract String format(Object aValue);
}