package de.gravitex.banking_core.entity.view;

import java.math.BigDecimal;
import java.time.LocalDate;

import de.gravitex.banking_core.entity.annotation.PresentMe;
import de.gravitex.banking_core.entity.base.NoIdEntity;
import de.gravitex.banking_core.formatter.DateTimeValueFormatter;
import de.gravitex.banking_core.formatter.DateValueFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "BOOKING_VIEW")
public class BookingView extends NoIdEntity {

	@Id
	private Long id;
	
	@PresentMe(valueFormatter = DateTimeValueFormatter.class, sortMe = true, order=1)
	private LocalDate bookingDate;
	
	@PresentMe(order = 10)
	private String text;
	
	@PresentMe(order = 20)
	private BigDecimal amount;
	
	@PresentMe(order = 30)
	private BigDecimal amountAfterBooking;
	
	@PresentMe(order = 40)
	private String purposeOfUse;
	
	@PresentMe(order = 50)
	private String purposeKey;
	
	private Long accountId;
	
	private Long tradingPartnerId;
	
	@PresentMe(order = 60)
	private String tradingPartnerKey;
	
	@PresentMe(order = 70)	
	private String customRemark;
	
	@PresentMe(order = 80)
	@Column(name = "file_name")
	private String importFileName;
	
	public String toString() {
		return "["+bookingDate+"] " + amount + " ("+purposeOfUse+")";		
	}
}