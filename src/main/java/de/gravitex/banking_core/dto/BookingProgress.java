package de.gravitex.banking_core.dto;

import java.time.LocalDate;
import java.util.List;

import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking_core.dto.base.BankingDto;
import de.gravitex.banking_core.service.util.BookingProgressByTradingKey;
import lombok.Data;

@Data
public class BookingProgress extends BankingDto {

	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private List<TradingPartner> tradingPartners;
	
	private List<BookingProgressByTradingKey> bookingProgressByTradingKeys;
}