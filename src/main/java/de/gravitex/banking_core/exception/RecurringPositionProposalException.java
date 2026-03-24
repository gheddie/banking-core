package de.gravitex.banking_core.exception;

public class RecurringPositionProposalException extends RuntimeException {

	private static final long serialVersionUID = -1443437169051801025L;
	
	public RecurringPositionProposalException(String message, Throwable t) {
		super(message, t);
	}
}