package de.gravitex.banking_core.controller.bookingimport;

import java.util.List;

import de.gravitex.banking_core.dto.BookingFileImportDto;
import lombok.Data;

@Data
public class ImportBookings {

	List<BookingFileImportDto> importedBookingFiles;
}