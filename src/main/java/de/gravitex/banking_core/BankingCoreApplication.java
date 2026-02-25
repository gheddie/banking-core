package de.gravitex.banking_core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import de.gravitex.banking_core.service.BookingService;

@SpringBootApplication
public class BankingCoreApplication {
	
	@Autowired
	BookingService bookingService;

	public static void main(String[] args) {
		SpringApplication.run(BankingCoreApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {	    
	    bookingService.importBookings();
	}
}