package de.gravitex.banking_core.service.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking_core.exception.BookingImportException;

public class ImportDescriptor {

	private static final String PROCESSED_BOOKINGS_DIRECTORY = "processed";

	private Account account;
	
	private String rootDirectory;

	public ImportDescriptor(Account account, String aRootDirectory) {
		super();
		this.account = account;
		this.rootDirectory = aRootDirectory;
	}

	public String buildImportPath() {
		return rootDirectory + "\\" + account.getCreditInstitute().getBic() + "_" + account.getIdentifier();
	}

	public Account getAccount() {
		return account;
	}

	public File getImportFile(String aImportFileName) {
		String importFilePath = buildImportPath() + "\\" + aImportFileName;
		if (!Files.exists(Paths.get(importFilePath))) {
			throw new BookingImportException("import file {" + importFilePath + "} does not exist!!!");
		}
		return new File(importFilePath);
	}

	public String getProcessedFilePath() {
		return buildImportPath() + "\\" + PROCESSED_BOOKINGS_DIRECTORY;
	}
}