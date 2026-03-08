package de.gravitex.banking_core.importer;

public class VolksbankCsvBookingImporter extends CsvBookingImporter {

	@Override
	protected String bookingDayDescriptor() {
		return "Buchungstag";
	}

	@Override
	protected String partnerNameDescriptor() {
		return "Name Zahlungsbeteiligter";
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
		return true;
	}
}