package de.gravitex.banking_core.controller.bookingimport;

import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class ImportFileBookings extends BankingDto {

	private BookingFileImportDto bookingFileImportDto;
}