package de.gravitex.banking_core.dto;

import java.time.LocalDate;
import java.util.List;

import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BookingCurrent extends BankingDto {

	private LocalDate fromDate;
	
	private LocalDate untilDate;
	
	private TradingPartner tradingPartner;
	
	private List<BookingCurrentItem> items;
}