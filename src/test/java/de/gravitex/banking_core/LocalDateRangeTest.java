package de.gravitex.banking_core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import de.gravitex.banking_core.service.util.LocalDateRange;

public class LocalDateRangeTest {

	@Test
	public void testLocalDateRange() {

		LocalDateRange range = LocalDateRange.forDates(LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 5));
		
		// before
		assertFalse(range.contains(LocalDate.of(2026, 10, 1)));
		assertFalse(range.contains(LocalDate.of(2026, 10, 2)));
		
		// the range
		assertTrue(range.contains(LocalDate.of(2026, 10, 3)));
		assertTrue(range.contains(LocalDate.of(2026, 10, 4)));
		assertTrue(range.contains(LocalDate.of(2026, 10, 5)));
		
		// after
		assertFalse(range.contains(LocalDate.of(2026, 10, 6)));
		assertFalse(range.contains(LocalDate.of(2026, 10, 7)));
	}
	
	@Test
	public void testLocalDateRangeDays() {
		
		LocalDateRange range = LocalDateRange.forDates(LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 7));
		
		assertEquals(5, range.getDays().size());
		
		assertEquals(LocalDate.of(2026, 10, 3), range.getDays().get(0));
		assertEquals(LocalDate.of(2026, 10, 4), range.getDays().get(1));
		assertEquals(LocalDate.of(2026, 10, 5), range.getDays().get(2));
		assertEquals(LocalDate.of(2026, 10, 6), range.getDays().get(3));
		assertEquals(LocalDate.of(2026, 10, 7), range.getDays().get(4));		
	}
}