package de.gravitex.banking_core.exception;

import de.gravitex.banking_core.exception.base.BankingException;

public class ImportDirectoryMandatoryException extends BankingException {
	
	private static final long serialVersionUID = -4267134820801968309L;

	public ImportDirectoryMandatoryException(String aMissingImportPath) {
		super(extracted(aMissingImportPath));
	}

	private static String extracted(String aMissingImportPath) {
		return "Import-Verzeichnis {"+aMissingImportPath+"} nicht vorhanden!!!";
	}
}