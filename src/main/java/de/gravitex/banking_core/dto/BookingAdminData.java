package de.gravitex.banking_core.dto;

import lombok.Data;

@Data
public class BookingAdminData {

	private String datasourceName;
	
	private String importRoot;
	
	private String databaseUrl;
	
	private String databaseDriverClass;
}