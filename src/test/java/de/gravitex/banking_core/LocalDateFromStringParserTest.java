package de.gravitex.banking_core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.gravitex.banking_core.service.util.LocalDateFromStringParser;
import de.gravitex.banking_core.service.util.DateRegexSample;
import de.gravitex.banking_core.util.StringHelper;

public class LocalDateFromStringParserTest {

	@Test
	public void testParseDateFromBookingFileName() {
		
		List<String> r1 = StringHelper.getIterations("WERNER", 3);
		assertEquals(4, r1.size());
		assertEquals("WER", r1.get(0));
		assertEquals("ERN", r1.get(1));
		assertEquals("RNE", r1.get(2));
		assertEquals("NER", r1.get(3));
		
		assertTrue(DateRegexSample.fromValues("^\\d{4}.\\d{2}.\\d{2}$", "2026.03.20", null).isValid());
		
		LocalDateFromStringParser parser = new LocalDateFromStringParser();

		assertEquals(null, parser.parseDate(null));
		assertEquals(null, parser.parseDate(""));
		
		assertEquals(LocalDate.of(2026, 3, 20), parser.parseDate("Umsaetze_DE74270925553505227900_2026.03.20.csv"));
		assertEquals(LocalDate.of(2026, 3, 20), parser.parseDate("2026.03.20"));
		assertEquals(LocalDate.of(2026, 3, 20), parser.parseDate("FOO.2026.03.20.BAR"));
		assertEquals(LocalDate.of(2026, 3, 20), parser.parseDate("2026.03.20.BAR"));
		assertEquals(LocalDate.of(2026, 3, 20), parser.parseDate("2026.03.20.BAR"));
	}
}