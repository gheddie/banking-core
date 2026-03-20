package de.gravitex.banking_core.service.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import de.gravitex.banking_core.util.StringHelper;

public class LocalDateFromStringParser {
	
	private static final List<DateRegexSample> REGEX_SAMPLES = new ArrayList<>();
	static {
		REGEX_SAMPLES.add(DateRegexSample.fromValues("^\\d{4}.\\d{2}.\\d{2}$", "2026.03.20", DateTimeFormatter.ofPattern("yyyy.MM.dd")));
	}

	public LocalDate parseDate(String aDateString) {
		if (StringHelper.isBlank(aDateString)) {
			return null;
		}
		for (DateRegexSample aRegexSample : REGEX_SAMPLES) {
			int sampleLength = aRegexSample.getSampleLength();
			List<String> iterations = StringHelper.getIterations(aDateString, sampleLength);
			for (String aIteration : iterations) {
				if (aRegexSample.matchesValue(aIteration)) {
					try {
						LocalDate parsed = LocalDate.parse(aIteration, aRegexSample.getDateTimeFormatter());
						return parsed;
					} catch (DateTimeParseException e) {
						// TODO: handle exception
					}					
				}
			}
		}		
		return null;
	}
}