package de.gravitex.banking_core.util.db.info;

import de.gravitex.banking_core.util.db.info.base.DatabaseTypeInfo;

public class H2DatabaseTypeInfo extends DatabaseTypeInfo {

	@Override
	public boolean shouldRunTests() {
		return true;
	}

	@Override
	public String getTypeDescription() {
		return "H2";
	}

	@Override
	public String getImportRootDirectory() {
		return "C:\\tmp\\testbankingimportroot";		
	}

	@Override
	public boolean moveProcessedImports() {
		return false;
	}
}