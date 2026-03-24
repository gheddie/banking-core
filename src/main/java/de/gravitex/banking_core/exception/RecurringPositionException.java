package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class RecurringPositionException extends BankingException {

	private static final long serialVersionUID = 8721789414333601636L;
	
	public RecurringPositionException(String message) {
		super(message);
	}
}