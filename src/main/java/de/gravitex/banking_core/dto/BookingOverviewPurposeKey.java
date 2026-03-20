package de.gravitex.banking_core.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.gravitex.banking.entity.Booking;
import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BookingOverviewPurposeKey extends BankingDto {

	private List<Booking> bookings = new ArrayList<Booking>();
	
	private String purposeKey;
	
	private int bookingCount;
	
	private BigDecimal totalSum = BigDecimal.ZERO;
	
	private BigDecimal bookingAverage = BigDecimal.ZERO;

	@JsonIgnore
	public void addBooking(Booking aBooking) {
		bookings.add(aBooking);
	}

	@JsonIgnore
	public BookingOverviewPurposeKey finish() {
		for (Booking aBooking : bookings) {
			totalSum = totalSum.add(aBooking.getAmount());
		}
		bookingCount = bookings.size();
		bookingAverage = averageAmounts(RoundingMode.HALF_UP);
		return this;
	}
	
	@JsonIgnore
	public BigDecimal averageAmounts(RoundingMode roundingMode) {
		List<BigDecimal> amounts = new ArrayList<>();
		for (Booking aBooking : bookings) {
			amounts.add(aBooking.getAmount());
		}
		BigDecimal average = amounts.stream()
			    .filter(Objects::nonNull)
			    .reduce(BigDecimal.ZERO, BigDecimal::add)
			    .divide(BigDecimal.valueOf(amounts.size()), 2, RoundingMode.HALF_UP);
		return average; 
	}
}