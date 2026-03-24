package de.gravitex.banking_core.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.gravitex.banking.entity.TradingPartner;
import lombok.Data;

@Data
public class RecurringPositionProposal {

	private List<TradingPartnerRecurringPositionProposal> tradingPartnerRecurringPositionProposals;
	
	private List<TradingPartner> tradingPartners;

	@JsonIgnore
	public TradingPartnerRecurringPositionProposal findByTradingKey(String aTradingKey) {
		for (TradingPartnerRecurringPositionProposal aProposal : tradingPartnerRecurringPositionProposals) {
			if (aProposal.getTradingPartner().getTradingKey().equals(aTradingKey)) {
				return aProposal;
			}
		}
		return null;
	}
}