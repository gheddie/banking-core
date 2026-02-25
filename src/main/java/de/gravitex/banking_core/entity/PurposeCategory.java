package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PurposeCategory extends IdEntity {

	@Column(nullable = false)
	private String purposeKey;
	
	public String toString() {
		return purposeKey;		
	}
}