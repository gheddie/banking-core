package de.gravitex.banking_core.dto;

import de.gravitex.banking.entity.annotation.PresentMe;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class UnprocessedBookingImport extends BankingDto {

	@PresentMe(order = 10)
	private String bookingFileName;
}