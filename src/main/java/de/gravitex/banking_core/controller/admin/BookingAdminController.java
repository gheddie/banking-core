package de.gravitex.banking_core.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingAdminController {
	
	@Value("${spring.datasource.username}")
	private String datasourceName;
	
	@Value("${import.rootdir}")
	private String importRoot;
	
	@Value("${spring.datasource.url}")
	private String databaseUrl;
	
	@Value("${spring.datasource.driver.class}")
	private String databaseDriverClass;

	@GetMapping(value = "bookingadmindata")
	public BookingAdminData getAdminData() {
		BookingAdminData bookingAdminData = new BookingAdminData();
		bookingAdminData.setDatasourceName(datasourceName);
		bookingAdminData.setImportRoot(importRoot);
		bookingAdminData.setDatabaseUrl(databaseUrl);
		bookingAdminData.setDatabaseDriverClass(databaseDriverClass);
		return bookingAdminData;
	}
}