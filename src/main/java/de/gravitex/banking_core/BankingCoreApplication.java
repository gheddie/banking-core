package de.gravitex.banking_core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.event.EventListener;

import de.gravitex.banking_core.service.BankingService;

@SpringBootApplication
@EntityScan(basePackages = {"de.gravitex.banking.entity", "de.gravitex.banking_core.entity.view"})
public class BankingCoreApplication {
	
	@Autowired
	BankingService bankingService;
	
	@Value("${import.initially}")
	private boolean importBookingsInitially;	

	public static void main(String[] args) {
		SpringApplication.run(BankingCoreApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		if (!importBookingsInitially) {
			return;
		}
	    bankingService.importBookings();
	}
}