package de.gravitex.banking_core.dto;

import java.util.List;

import de.gravitex.banking.entity.Booking;
import lombok.Data;

@Data
public class BookingImportSummary {

	private String bookingFileName;
	
	private List<Booking> importedBookings;
	
	private List<Booking> ignoredBookings;
}