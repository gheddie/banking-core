package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class MergeTradingPartnersException extends BankingException {
	
	private static final long serialVersionUID = 213981882562030955L;

	public MergeTradingPartnersException(String message) {
		super(message);
	}
}