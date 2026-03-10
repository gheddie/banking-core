package de.gravitex.banking_core.dto;

import java.util.List;

import de.gravitex.banking_core.dto.base.BankingDto;
import de.gravitex.banking_core.entity.TradingPartner;
import lombok.Data;

@Data
public class MergeTradingPartners extends BankingDto {

	private List<TradingPartner> partnersToMerge;
	
	private String newTradingKey;
}