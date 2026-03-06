package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class BudgetPlanningException extends BankingException {
	
	private static final long serialVersionUID = -9099454735376862678L;

	public BudgetPlanningException(String message) {
		super(message);
	}
}