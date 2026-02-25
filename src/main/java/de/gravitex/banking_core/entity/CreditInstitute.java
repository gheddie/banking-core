package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class CreditInstitute extends IdEntity {

	@PresentMe(order = 0)
	@Column(nullable = false, unique = true)
	private String name;
	
	@PresentMe(order = 10)
	@Column(nullable = false, unique = true)
	private String bic;
	
	public String toString() {
		return name + " ("+bic+")";		
	}
}