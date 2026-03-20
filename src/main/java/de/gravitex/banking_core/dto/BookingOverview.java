package de.gravitex.banking_core.dto;

import java.time.LocalDate;
import java.util.List;

import de.gravitex.banking.entity.Account;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BookingOverview extends BankingDto {

	private Account account;
	
	private LocalDate fromDate;
	
	private LocalDate untilDate;
	
	private List<BookingOverviewPurposeKey> bookingOverviewTradingKeys;
}