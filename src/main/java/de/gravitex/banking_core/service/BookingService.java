package de.gravitex.banking_core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.entity.BookingImport;
import de.gravitex.banking_core.entity.BookingImportItem;
import de.gravitex.banking_core.entity.ImportType;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.importer.CsvBookingImporter;
import de.gravitex.banking_core.importer.base.BookingImporter;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingImportItemRepository;
import de.gravitex.banking_core.repository.BookingImportRepository;
import de.gravitex.banking_core.repository.BookingRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;
import de.gravitex.banking_core.util.StringHelper;
import jakarta.transaction.Transactional;

@Service
public class BookingService {

	@Value("${import.rootdir}")
	private String rootDirectory;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private TradingPartnerRepository tradingPartnerRepository;
	
	@Autowired
	private BookingImportRepository bookingImportRepository;
	
	@Autowired
	private BookingImportItemRepository bookingImportItemRepository;

	private static final Map<ImportType, BookingImporter> IMPORTERS = new HashMap<>();
	static {
		IMPORTERS.put(ImportType.CSV, new CsvBookingImporter());
	}

	public void importBookings() {
		System.out.println("importing all bookings [" + rootDirectory + "]...");
		for (Account account : accountRepository.findAll()) {
			ImportDescriptor importDescriptor = getImportDescriptor(account);
			String importPath = importDescriptor.buildImportPath();
			System.out.println("importing bookings for account [" + account + "] --> Pfad: " + importPath + " ["
					+ account.getImportType() + "]");
			processFiles(importPath, account, importDescriptor);
		}
	}

	@Transactional
	private void processFiles(String directoryPath, Account account, ImportDescriptor importDescriptor) {
		File directory = new File(directoryPath);
		System.out.println(directory);
		File[] listedFiles = directory.listFiles();
		if (listedFiles != null && listedFiles.length > 0) {
			for (File file : listedFiles) {
				processFile(account, file, importDescriptor);
			}
		}
	}

	private void processFile(Account account, File file, ImportDescriptor importDescriptor) {
		List<Booking> bookings = getImporter(account.getImportType()).generateBookings(file);
		if (bookings != null && !bookings.isEmpty()) {
			List<Booking> newBookings = new ArrayList<>();
			for (Booking booking : bookings) {
				booking.setTradingPartner(getOrCreateTradingPartner(booking));
				booking.setAccount(account);
				newBookings.add(booking);
			}
			List<Booking> persistedBookings = persistNewBookings(newBookings, importDescriptor);
			if (persistedBookings == null || persistedBookings.isEmpty()) {
				System.out.println("Keine neuen Umsätze in Datei[" + file.getName() + "] --> kein Import erstellt!!!");
			}
			createImport(file, account, persistedBookings);
		}
	}

	private BookingImport createImport(File aImportFile, Account account, List<Booking> aBookings) {
		BookingImport bookingImport = new BookingImport();
		bookingImport.setFileName(aImportFile.getName());
		bookingImport.setAccount(account);
		bookingImport.setImportDate(new Date());
		BookingImport createdBookingImport = bookingImportRepository.save(bookingImport);
		int aItemPos = 0;
		for (Booking aBooking : aBookings) {			
			createImportItem(aBooking, createdBookingImport, aItemPos);
			aItemPos++;
		}
		return createdBookingImport;
	}

	private void createImportItem(Booking aBooking, BookingImport aBookingImport, int aItemPos) {
		BookingImportItem item = new BookingImportItem();
		item.setBooking(aBooking);
		item.setBookingImport(aBookingImport);
		item.setItemPos(aItemPos);
		bookingImportItemRepository.save(item);
	}

	private List<Booking> persistNewBookings(List<Booking> newBookings, ImportDescriptor importDescriptor) {
		BookingFilter filter = new BookingFilter(importDescriptor.getAccount());
		List<Booking> existingAccountBookings = bookingRepository.findByAccount(importDescriptor.getAccount());
		filter.mapExistingBookingKeys(existingAccountBookings);
		List<Booking> persistedBookings = new ArrayList<>();
		for (Booking newBooking : newBookings) {
			if (!filter.hasBooking(newBooking)) {
				persistedBookings.add(bookingRepository.save(newBooking));
			}
		}
		filter.summarize();		
		return persistedBookings;
	}

	private TradingPartner getOrCreateTradingPartner(Booking booking) {
		TradingPartner partner = tradingPartnerRepository.findByTradingKey(booking.getTradingPartnerKey());
		if (partner == null) {
			TradingPartner newPartner = new TradingPartner();
			newPartner.setTradingKey(booking.getTradingPartnerKey());
			tradingPartnerRepository.save(newPartner);
			return newPartner;
		} else {
			return partner;
		}
	}

	private BookingImporter getImporter(ImportType importType) {
		return IMPORTERS.get(importType);
	}

	private ImportDescriptor getImportDescriptor(Account account) {

		/*
		 * new ImportDescriptor(account).buildImportPath(); return rootDirectory +
		 * "\\" + account.getCreditInstitute().getBic() + "_" + account.getIdentifier();
		 */

		return new ImportDescriptor(account);
	}

	private class ImportDescriptor {

		private Account account;

		public ImportDescriptor(Account account) {
			super();
			this.account = account;
		}

		public String buildImportPath() {
			return rootDirectory + "\\" + account.getCreditInstitute().getBic() + "_" + account.getIdentifier();
		}

		public Account getAccount() {
			return account;
		}
	}

	private class BookingFilter {

		private Set<String> existingKeys = new HashSet<>();

		private int ignoredBookings = 0;

		private int newBookings = 0;

		private Account account;

		public BookingFilter(Account account) {
			super();
			this.account = account;
		}

		public void mapExistingBookingKeys(List<Booking> aExistingAccountBookings) {
			for (Booking aExistingAccountBooking : aExistingAccountBookings) {
				String bookingKey = createBookingKey(aExistingAccountBooking);
				if (existingKeys.contains(bookingKey)) {
					throw new IllegalArgumentException("booking key [" + bookingKey + "] is not unique!!!");
				}
				existingKeys.add(bookingKey);
			}
		}

		public void summarize() {
			System.out.println(newBookings + " new bookings imported for account --> " + account);
			System.out.println(ignoredBookings + " bookings ignored (NOT imported) for account --> " + account);
		}

		public boolean hasBooking(Booking newBooking) {
			String newBookingKey = createBookingKey(newBooking);
			boolean result = existingKeys.contains(newBookingKey);
			if (result) {
				ignoredBookings++;
			} else {
				newBookings++;
			}
			return result;
		}

		private String createBookingKey(Booking aBooking) {

			List<String> tmp = new ArrayList<>();

			// self
			tmp.add(String.valueOf(aBooking.getBookingDate().getTime()));
			tmp.add(aBooking.getText());
			tmp.add(aBooking.getPurposeOfUse());
			tmp.add(aBooking.getAmount().toString());

			// account
			tmp.add(aBooking.getAccount().getIdentifier());
			tmp.add(aBooking.getAccount().getName());

			// institute
			tmp.add(aBooking.getAccount().getCreditInstitute().getName());

			// trading partner
			tmp.add(aBooking.getTradingPartner().getTradingKey());

			String key = StringHelper.createHash(StringHelper.seperateList(tmp.toArray(new String[tmp.size()]), "@"));

			return key;
		}
	}
}