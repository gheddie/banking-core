package de.gravitex.banking_core.dto;

import java.util.List;

import de.gravitex.banking_core.entity.Booking;
import lombok.Data;

@Data
public class BookingFileImportDto {
	
	private String fileName;

	List<Booking> importedBookings;
}