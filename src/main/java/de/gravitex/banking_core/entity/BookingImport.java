package de.gravitex.banking_core.entity;

import java.util.Date;

import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class BookingImport extends IdEntity {

	@Column(nullable = false)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Account account;
	
	@Column(nullable = false)
	private Date importDate;
}