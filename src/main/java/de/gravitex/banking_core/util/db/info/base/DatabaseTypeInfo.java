package de.gravitex.banking_core.util.db.info.base;

public abstract class DatabaseTypeInfo {

	public abstract boolean shouldRunTests();

	public abstract String getTypeDescription();

	public abstract String getImportRootDirectory();

	public abstract boolean moveProcessedImports();
}