package de.gravitex.banking_core.entity;

import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class BookingImportItem extends IdEntity {

	@ManyToOne
	@JoinColumn(nullable = false)
	private Booking booking;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private BookingImport bookingImport;
	
	@Column(nullable = false)
	private int itemPos;
}