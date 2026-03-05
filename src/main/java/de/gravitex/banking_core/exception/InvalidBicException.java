package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class InvalidBicException extends BankingException {
	
	private static final long serialVersionUID = -4038799620785256047L;

	public InvalidBicException(String message) {
		super(message);
	}
}