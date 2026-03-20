package de.gravitex.banking_core.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.gravitex.banking_core.dto.base.BankingDto;
import lombok.Data;

@Data
public class BookingCurrentItem extends BankingDto {

	private LocalDate date;
	
	private BigDecimal amount;

	@JsonIgnore
	public void acceptAmount(BigDecimal newAmount) {
		amount = amount.add(newAmount);
	}

	@JsonIgnore
	public boolean hasAmount() {
		return (amount.abs().compareTo(BigDecimal.ZERO) != 0);
	}
}