package de.gravitex.banking_core.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.importer.base.BookingImporter;

public class CsvBookingImporter extends BookingImporter {

	private static final String DELIMITER = ";";

	@Override
	public List<Booking> generateBookings(File file) {
		CsvWrapper wrapper = new CsvWrapper(file);
		List<Booking> bookings = new ArrayList<>();
		for (CsvLine aCsvLine : wrapper.getLinesOrdered()) {
			
			Booking booking = new Booking();
			
			booking.setText(aCsvLine.getValueByKey("Buchungstext"));
			booking.setPurposeOfUse(aCsvLine.getValueByKey("Verwendungszweck"));		
			booking.setAmount(getBigDecimal(aCsvLine.getValueByKey("Betrag")));
			booking.setAmountAfterBooking(getBigDecimal(aCsvLine.getValueByKey("Saldo nach Buchung")));
			booking.setTradingPartnerKey(aCsvLine.getValueByKey("Name Zahlungsbeteiligter"));
			booking.setBookingDate(parseLocalDate(aCsvLine.getValueByKey("Buchungstag")));
			
			bookings.add(booking);
		}
		return bookings;
	}

	private class CsvWrapper {

		private File csvFile;

		private List<String> lines;

		private String[] headers;

		private Map<Integer, CsvLine> mappedValues = new HashMap<>();

		public CsvWrapper(File aCsvFile) {
			super();
			this.csvFile = aCsvFile;
			lines = readImportLines(csvFile);
			determineHeaders();
			for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
				mappedValues.put(Integer.valueOf(lineIndex),
						new CsvLine(mapValues(lines.get(lineIndex), lineIndex), lineIndex));
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public List<CsvLine> getLinesOrdered() {
			List<Integer> lineKeys = new ArrayList(mappedValues.keySet());
			Collections.sort(lineKeys);
			List<CsvLine> ordered = new ArrayList<>();
			for (Integer index : lineKeys) {
				ordered.add(mappedValues.get(index));
			}
			return ordered;
		}

		private Map<String, String> mapValues(String aCsvLine, int lineIndex) {
			Map<String, String> values = new HashMap<>();
			String[] spl = aCsvLine.split(DELIMITER);
			int rowIndex = 0;
			for (String a : spl) {
				values.put(headers[rowIndex], a);
				rowIndex++;
			}
			return values;
		}

		private void determineHeaders() {
			headers = lines.get(0).split(DELIMITER);
		}
	}

	private class CsvLine {

		private Map<String, String> mappedValues;

		private int lineIndex;

		public CsvLine(Map<String, String> aMappedValues, int aLineIndex) {
			super();
			this.mappedValues = aMappedValues;
			this.lineIndex = aLineIndex;
		}
		
		public String getValueByKey(String aKey) {
			if (!mappedValues.containsKey(aKey)) {
				throw new IllegalArgumentException("key ["+aKey+"] not present!!!");
			}
			return mappedValues.get(aKey);
		}

		@Override
		public String toString() {
			return "["+lineIndex+"] --> " + mappedValues;
		}
	}
}