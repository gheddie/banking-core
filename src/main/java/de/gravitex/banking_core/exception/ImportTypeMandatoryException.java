package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class ImportTypeMandatoryException extends BankingException {

	private static final long serialVersionUID = -1552929079771409674L;
	
	public ImportTypeMandatoryException() {
		super("Import-Typ muss gesetzt sein!!!");
	}
}