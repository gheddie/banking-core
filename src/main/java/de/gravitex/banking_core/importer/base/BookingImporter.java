package de.gravitex.banking_core.importer.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.gravitex.banking_core.entity.Booking;

public abstract class BookingImporter {
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

	public abstract List<Booking> generateBookings(File file);
	
	protected List<String> readImportLines(File file) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	lines.add(line);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;		
	}
	
	protected BigDecimal getBigDecimal(String aValue) {
		aValue = aValue.replaceAll(",", ".");
		return new BigDecimal(aValue);
	}
	
	protected Date getDate(String aValue) {
		// return new Date();
		try {
			Date result = formatter.parse(aValue);
			return result;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}