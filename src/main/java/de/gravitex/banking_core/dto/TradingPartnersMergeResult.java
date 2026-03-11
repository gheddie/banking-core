package de.gravitex.banking_core.dto;

import java.util.ArrayList;
import java.util.List;

import de.gravitex.banking.entity.Booking;
import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class TradingPartnersMergeResult extends BankingDto {

	private List<Booking> switchedBookings = new ArrayList<>();

	private TradingPartner newTradingPartner;

	public void addSwitchedBooking(Booking aBookingToSwitch) {
		switchedBookings.add(aBookingToSwitch);
	}

	public String summarize() {
		return switchedBookings.size() + " umgebucht auf Partner {" + newTradingPartner.getTradingKey() + "}";
	}
}