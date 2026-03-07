package de.gravitex.banking_core.importer.exception.csv.base;

public abstract class CsvProcessingException extends Exception {
	
	private static final long serialVersionUID = 2708825405101906334L;

	public CsvProcessingException(String message) {
		super(message);
	}
}