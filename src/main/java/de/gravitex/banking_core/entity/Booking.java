package de.gravitex.banking_core.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.gravitex.banking_core.entity.annotation.EnableEdit;
import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.annotation.util.EditType;
import de.gravitex.banking_core.entity.base.IdEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Booking extends IdEntity {

	@PresentMe(order = 10)
	@Column(nullable = false)
	@EnableEdit(type = EditType.NEVER)
	private String text;
	
	@PresentMe(order = 20)
	@Column(nullable = false)
	@EnableEdit(type = EditType.NEVER)
	private String purposeOfUse;
	
	@PresentMe(order = 30)
	private String customRemark;
	
	@PresentMe(order = 40)
	@Column(nullable = false)
	private BigDecimal amount;
	
	@ManyToOne
	private PurposeCategory purposeCategory;
	
	@PresentMe(order = 50)
	@Column(nullable = false)
	private BigDecimal amountAfterBooking;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@EnableEdit(type = EditType.NEVER)
	private TradingPartner tradingPartner;
	
	@PresentMe(order = 60)
	@Column(nullable = false)
	private LocalDate bookingDate;	
	
	@ManyToOne
	@PresentMe(order = 70)
	@JoinColumn(nullable = false)
	// @EnableEdit(type = EditType.NEVER)
	private Account account;
	
	@Transient
	@EnableEdit(type = EditType.NEVER)
	private String tradingPartnerKey;
}