package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.annotation.Creatable;
import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@Creatable
public class PurposeCategory extends IdEntity {

	@Column(nullable = false, unique = true)
	@PresentMe(order = 10, filterMe = true)
	private String purposeKey;
	
	public String toString() {
		return purposeKey;		
	}
}