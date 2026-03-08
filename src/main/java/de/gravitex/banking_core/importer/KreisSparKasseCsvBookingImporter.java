package de.gravitex.banking_core.importer;

import java.time.format.DateTimeFormatter;

public class KreisSparKasseCsvBookingImporter extends CsvBookingImporter {

	@Override
	protected String bookingDayDescriptor() {
		return "Buchungstag";
	}

	@Override
	protected String partnerNameDescriptor() {
		return "Beguenstigter/Zahlungspflichtiger";
	}

	@Override
	protected String amountPostBookingDescriptor() {
		return "Saldo nach Buchung";
	}

	@Override
	protected String amountDescriptor() {
		return "Betrag";
	}

	@Override
	protected String purposeOfUseDescriptor() {
		return "Verwendungszweck";
	}

	@Override
	protected String bookingTextDescriptor() {
		return "Buchungstext";
	}

	@Override
	protected boolean amountAfterBookingPresent() {
		return false;
	}

	@Override
	protected boolean isDataQuoted() {
		return true;
	}

	@Override
	protected DateTimeFormatter initDateFormatter() {
		return DateTimeFormatter.ofPattern("dd.MM.yy");
	}
}