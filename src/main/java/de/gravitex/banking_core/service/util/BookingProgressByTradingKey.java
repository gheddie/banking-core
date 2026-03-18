package de.gravitex.banking_core.service.util;

import java.util.List;

import de.gravitex.banking.entity.Booking;
import lombok.Data;

@Data
public class BookingProgressByTradingKey {

	private String tradingPartnerKey;
	
	private List<Booking> bookings;

	public BookingProgressByTradingKey(String aTradingPartnerKey, List<Booking> aBookings) {
		super();
		this.tradingPartnerKey = aTradingPartnerKey;
		this.bookings = aBookings;
	}
}