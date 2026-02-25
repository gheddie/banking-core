package de.gravitex.banking_core.entity;

import java.math.BigDecimal;
import java.util.Date;

import de.gravitex.banking_core.entity.annotation.PresentMe;
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

	@PresentMe(order = 0)
	@Column(nullable = false)
	private String text;
	
	@PresentMe(order = 0)
	@Column(nullable = false)
	private String purposeOfUse;
	
	@PresentMe(order = 0)
	private String customRemark;
	
	@PresentMe(order = 0)
	@Column(nullable = false)
	private BigDecimal amount;
	
	@PresentMe(order = 0)
	@Column(nullable = false)
	private BigDecimal amountAfterBooking;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private TradingPartner tradingPartner;
	
	@PresentMe(order = 0)
	@Column(nullable = false)
	private Date bookingDate;	
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Account account;
	
	@Transient
	private String tradingPartnerKey;
}