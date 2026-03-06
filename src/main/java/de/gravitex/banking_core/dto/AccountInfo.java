package de.gravitex.banking_core.dto;

import java.math.BigDecimal;

import de.gravitex.banking_core.dto.base.BankingDto;
import de.gravitex.banking_core.entity.Account;
import lombok.Data;

@Data
public class AccountInfo extends BankingDto {

	private Account account;
	
	private BigDecimal actualAmount;
}