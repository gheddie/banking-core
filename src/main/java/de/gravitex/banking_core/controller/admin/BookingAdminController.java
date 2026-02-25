package de.gravitex.banking_core.controller.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingAdminController {
	
	@Value("${spring.datasource.username}")
	private String datasourceName;
	
	@Value("${import.rootdir}")
	private String importRoot;

	@RequestMapping(value = "bookingadmindata", method = RequestMethod.GET)
	public BookingAdminData getAdminData() {
		BookingAdminData bookingAdminData = new BookingAdminData();
		bookingAdminData.setDatasourceName(datasourceName);
		bookingAdminData.setImportRoot(importRoot);
		return bookingAdminData;
	}
}