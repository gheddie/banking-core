package de.gravitex.banking_core.exception;

import java.util.Optional;

import de.gravitex.banking.entity.base.IdEntity;
import de.gravitex.banking_core.exception.base.BankingException;

public class OptionalNotPresentException extends BankingException {
	
	private static final long serialVersionUID = -114770439457364196L;

	public OptionalNotPresentException(Optional<? extends IdEntity> aOptional, Class<? extends IdEntity> entityClass) {
		super(buildMessage(aOptional, entityClass));
	}

	private static String buildMessage(Optional<? extends IdEntity> aOptional, Class<? extends IdEntity> entityClass) {
		return "Entität vom Typ {" + entityClass.getSimpleName() + "} nicht vorhanden!!!";
	}
}