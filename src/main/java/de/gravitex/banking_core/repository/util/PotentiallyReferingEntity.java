package de.gravitex.banking_core.repository.util;

import java.text.MessageFormat;
import java.util.List;

import de.gravitex.banking_core.entity.base.IdEntity;

public class PotentiallyReferingEntity {

	private static final String QUERY_TEMPLATE = "select b from {0} b where b.{1} = :param";

	private Class<? extends IdEntity> referenceClass;
	
	private String fieldName;

	private List<? extends IdEntity> referringEntities;

	public PotentiallyReferingEntity(Class<? extends IdEntity> aReferenceClass, String aFieldName) {
		super();
		this.referenceClass = aReferenceClass;
		this.fieldName = aFieldName;
	}

	public String buildQueryString() {
		String str = MessageFormat.format(QUERY_TEMPLATE, referenceClass.getSimpleName(), fieldName);
		return str;
	}

	public void acceptReferringEntities(List<?extends IdEntity> aReferringEntities) {
		this.referringEntities = aReferringEntities;
	}

	public boolean hasReferingEntites() {
		return (referringEntities != null && !referringEntities.isEmpty());
	}

	public String format() {
		return referenceClass.getSimpleName() + " ("+referringEntities.size()+")";
	}
}