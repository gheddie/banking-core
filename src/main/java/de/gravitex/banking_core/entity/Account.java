package de.gravitex.banking_core.entity;

import java.time.LocalDate;

import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Account extends IdEntity {
	
	@PresentMe(order = 10)
	@Column(nullable = false)
	private String name;
	
	@PresentMe(order = 20)	
	@Column(nullable = false)
	private String identifier;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ImportType importType;
	
	@Column(nullable = false)
	private LocalDate latestBookingDate;
	
	@PresentMe(order = 30)
	@ManyToOne
	private CreditInstitute creditInstitute;
	
	public String toString() {
		return name + " ("+creditInstitute.getName()+"@"+creditInstitute.getBic()+")";		
	}	
}