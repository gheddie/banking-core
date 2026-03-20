package de.gravitex.banking_core.service.util;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateRegexSample {

	private String regex;
	
	private String sample;

	private Pattern pattern;

	private DateTimeFormatter dateTimeFormatter;

	private DateRegexSample(String aRegex, String aSample, DateTimeFormatter aDateTimeFormatter) {
		
		super();
		
		this.regex = aRegex;
		this.sample = aSample;
		
		this.dateTimeFormatter = aDateTimeFormatter;
		
		pattern = Pattern.compile(regex);
	}

	public static DateRegexSample fromValues(String aRegex, String aSample, DateTimeFormatter aDateTimeFormatter) {
		return new DateRegexSample(aRegex, aSample, aDateTimeFormatter);
	}

	public boolean isValid() {
		return pattern.matcher(sample).matches();
	}
	
	public String getSample() {
		return sample;
	}

	public int getSampleLength() {
		return sample.length();
	}

	public boolean matchesValue(String aValue) {
		return pattern.matcher(aValue).matches();
	}
	
	public DateTimeFormatter getDateTimeFormatter() {
		return dateTimeFormatter;
	}
}