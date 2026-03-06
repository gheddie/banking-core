package de.gravitex.banking_core.dto;

import java.math.BigDecimal;

import de.gravitex.banking_core.dto.base.BankingDto;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.annotation.PresentMe;
import lombok.Data;

@Data
public class AccountInfo extends BankingDto {

	@PresentMe(order = 10)
	private Account account;
	
	@PresentMe(order = 20)
	private BigDecimal actualAmount;
}