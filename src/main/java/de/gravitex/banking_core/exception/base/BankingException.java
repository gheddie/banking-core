package de.gravitex.banking_core.exception.base;

public abstract class BankingException extends RuntimeException {
	
	private static final long serialVersionUID = 6739011191949590929L;

	public BankingException(String message) {
		super(message);
	}
}