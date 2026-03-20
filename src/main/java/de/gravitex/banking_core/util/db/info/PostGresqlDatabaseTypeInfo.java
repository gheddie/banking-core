package de.gravitex.banking_core.util.db.info;

import de.gravitex.banking_core.util.db.info.base.DatabaseTypeInfo;

public class PostGresqlDatabaseTypeInfo extends DatabaseTypeInfo {

	@Override
	public boolean shouldRunTests() {
		return false;
	}

	@Override
	public String getTypeDescription() {
		return "PostGresql";
	}

	@Override
	public String getImportRootDirectory() {
		return "C:\\tmp\\bankingimportroot";
	}

	@Override
	public boolean moveProcessedImports() {
		return true;
	}
}