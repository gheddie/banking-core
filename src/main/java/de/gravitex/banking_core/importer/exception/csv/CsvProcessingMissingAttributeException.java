package de.gravitex.banking_core.importer.exception.csv;

import de.gravitex.banking_core.importer.exception.csv.base.CsvProcessingException;

public class CsvProcessingMissingAttributeException extends CsvProcessingException {

	private static final long serialVersionUID = -8786604326047009232L;
	
	private String attribute;

	public CsvProcessingMissingAttributeException(String attribute) {
		super("");
		this.attribute = attribute;
	}
	
	public String getAttribute() {
		return attribute;
	}
}