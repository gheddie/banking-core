package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.annotation.Creatable;
import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Creatable
public class Account extends IdEntity {
	
	@PresentMe(order = 10, filterMe = true)
	@Column(nullable = false)
	private String name;
	
	@PresentMe(order = 20, filterMe = true)	
	@Column(nullable = false)
	private String identifier;
	
	@PresentMe(order = 30)
	@ManyToOne
	@JoinColumn(nullable = false)
	private CreditInstitute creditInstitute;
	
	public String toString() {
		return name + " ("+creditInstitute.getName()+"@"+creditInstitute.getBic()+")";		
	}	
}