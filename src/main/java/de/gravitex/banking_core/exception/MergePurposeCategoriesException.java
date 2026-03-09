package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class MergePurposeCategoriesException extends BankingException {
	
	private static final long serialVersionUID = 213981882562030955L;

	public MergePurposeCategoriesException(String message) {
		super(message);
	}
}