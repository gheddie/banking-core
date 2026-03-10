package de.gravitex.banking_core.repository.util;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.entity.base.IdEntity;
import de.gravitex.banking_core.exception.ReferingEntitiesException;

public class PotientallyReferenced {

	private IdEntity entity;
	
	private List<PotentiallyReferingEntity> potentiallyReferringEntites = new ArrayList<>();

	private PotientallyReferenced(IdEntity aEntity) {
		super();
		this.entity = aEntity;
	}

	public IdEntity getEntity() {
		return entity;
	}

	public static PotientallyReferenced forEntity(IdEntity aEntity) {
		return new PotientallyReferenced(aEntity);
	}

	public PotientallyReferenced withPotentiallyReferringEntity(Class<? extends IdEntity> aReferrenceClass, String aFieldName) {
		potentiallyReferringEntites.add(new PotentiallyReferingEntity(aReferrenceClass, aFieldName));
		return this;
	}
	
	public List<PotentiallyReferingEntity> getPotentiallyReferringEntites() {
		return potentiallyReferringEntites;
	}

	public void failForActualReferences() {
		List<PotentiallyReferingEntity> failings = getFailings();
		if (failings.isEmpty()) {
			return;
		}
		throw new ReferingEntitiesException(entity, failings);
	}

	private List<PotentiallyReferingEntity> getFailings() {
		List<PotentiallyReferingEntity> failings = new ArrayList<>();
		for (PotentiallyReferingEntity aPotentiallyRefering : potentiallyReferringEntites) {
			if (aPotentiallyRefering.hasReferingEntites()) {
				failings.add(aPotentiallyRefering);
			}
		}
		return failings;
	}

	public List<? extends IdEntity> getReferringEntities(Class<Booking> entityClass) {
		for (PotentiallyReferingEntity aPotentiallyRefering : potentiallyReferringEntites) {
			if (aPotentiallyRefering.isForEntityClass(entityClass)) {
				return aPotentiallyRefering.getReferringEntities();
			}
		}
		return new ArrayList<>();		
	}
}