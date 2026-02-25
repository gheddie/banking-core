package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class TradingPartner extends IdEntity {

	@PresentMe(sortMe = true, order = 10)
	@Column(nullable = false)
	private String tradingKey;
	
	@PresentMe(order = 20)
	@ManyToOne
	private PurposeCategory purposeCategory;
	
	public String toString() {
		return tradingKey;		
	}
}