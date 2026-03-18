package de.gravitex.banking_core.dto;

import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class ImportFileBookings extends BankingDto {

	private BookingFileImportDto bookingFileImportDto;
}