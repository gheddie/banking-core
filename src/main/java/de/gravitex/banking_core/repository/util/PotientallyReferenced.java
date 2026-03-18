package de.gravitex.banking_core.repository.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.gravitex.banking.entity.Booking;
import de.gravitex.banking.entity.base.IdEntity;
import de.gravitex.banking_core.exception.ReferingEntitiesException;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;

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

	@SuppressWarnings("unchecked")
	public PotientallyReferenced autoDetectPotentiallyReferrings(Set<EntityType<?>> aEntityTypes) {
		for (ManagedType<?> aManagedType : aEntityTypes) {
			Class<?> clazz = aManagedType.getJavaType();
			for (Field aField : clazz.getDeclaredFields()) {
				Class<?> fieldClass = aField.getType();
				if (fieldClass.equals(entity.getClass())) {
					withPotentiallyReferringEntity((Class<? extends IdEntity>) clazz, aField.getName());
				}
			}			
		}			
		return this;
	}
}