package de.gravitex.banking_core.dto;

import de.gravitex.banking.entity.RecurringPosition;
import de.gravitex.banking.entity.TradingPartner;
import lombok.Data;

@Data
public class TradingPartnerRecurringPositionProposal {

	private TradingPartner tradingPartner;
	
	private RecurringPosition recurringPosition;
}