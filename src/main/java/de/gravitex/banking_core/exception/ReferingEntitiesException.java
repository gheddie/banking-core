package de.gravitex.banking_core.exception;

import java.util.List;

import de.gravitex.banking_core.entity.base.IdEntity;
import de.gravitex.banking_core.exception.base.BankingException;
import de.gravitex.banking_core.repository.util.PotentiallyReferingEntity;

public class ReferingEntitiesException extends BankingException {

	private static final long serialVersionUID = 8375436531443077473L;
	
	public ReferingEntitiesException(IdEntity aReferredEntity, List<PotentiallyReferingEntity> aFailings) {
		super(buildMessage(aReferredEntity, aFailings));
	}

	private static String buildMessage(IdEntity aReferredEntity, List<PotentiallyReferingEntity> aFailings) {		
		StringBuffer buffer = new StringBuffer()
				.append(aReferredEntity.getClass().getSimpleName() + " wird referenziert!").append("\n");
		for (PotentiallyReferingEntity aFailing : aFailings) {
			buffer.append(aFailing.format());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}
