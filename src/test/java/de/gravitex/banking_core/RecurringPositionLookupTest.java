package de.gravitex.banking_core;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class RecurringPositionLookupTest {

	@Test
	void testRecurringPositionLookup() {

		LocalDate from = LocalDate.of(2025, 3, 28);
		LocalDate to = LocalDate.of(2026, 2, 25);
		
		// assertEquals(12, RecurringPositionLookup.initFor(RecurringInterval.MONTHLY, from, to, new TradingPartner()).getRanges().size());
	}
}