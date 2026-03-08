package de.gravitex.banking_core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.gravitex.banking_core.util.StringHelper;

public class StringHelperTest {

	@Test
	void testDebracket() {
		
		String unbracketed = "unbracketed";
		String bracketed = "\"bracketed\"";
		
		assertEquals("bracketed", StringHelper.debracket(bracketed));
		assertEquals(unbracketed, StringHelper.debracket(unbracketed));
		
		assertEquals("", StringHelper.debracket(null));
		assertEquals("", StringHelper.debracket(""));
	}
}