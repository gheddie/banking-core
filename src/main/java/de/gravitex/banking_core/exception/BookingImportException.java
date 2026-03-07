package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class BookingImportException extends BankingException {
	
	private static final long serialVersionUID = 1920056931481505799L;

	public BookingImportException(String message) {
		super(message);
	}
}