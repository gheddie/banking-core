package de.gravitex.banking_core.dto;

import java.util.List;

import lombok.Data;

@Data
public class ImportBookings {

	List<BookingFileImportDto> importedBookingFiles;
}