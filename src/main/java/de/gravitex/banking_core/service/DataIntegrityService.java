package de.gravitex.banking_core.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import de.gravitex.banking.entity.base.IdEntity;
import de.gravitex.banking_core.exception.OptionalNotPresentException;
import de.gravitex.banking_core.repository.util.PotentiallyReferingEntity;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;

@Service
public class DataIntegrityService {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public PotientallyReferenced satisfyPotientallyReferenced(PotientallyReferenced aPotientallyReferenced) {
		for (PotentiallyReferingEntity aReferringEntity : aPotientallyReferenced.getPotentiallyReferringEntites()) {
			aReferringEntity.acceptReferringEntities(entityManager.createQuery(aReferringEntity.buildQueryString())
					.setParameter("param", aPotientallyReferenced.getEntity()).getResultList());
		}
		return aPotientallyReferenced;
	}

	public void assertOptionalPresent(Optional<? extends IdEntity> aOptional, Class<? extends IdEntity> entityClass) {
		if (!aOptional.isPresent()) {
			throw new OptionalNotPresentException(aOptional, entityClass);
		}
	}

	public Set<EntityType<?>> getEntityTypes() {
		return entityManager.getMetamodel().getEntities();
	}
}