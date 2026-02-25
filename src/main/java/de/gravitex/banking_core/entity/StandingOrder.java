package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class StandingOrder extends IdEntity {

	@PresentMe(sortMe = true, order = 10)
	@Column(nullable = false)
	private String description;
	
	public String toString() {
		return description;		
	}
}