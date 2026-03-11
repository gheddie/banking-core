package de.gravitex.banking_core.service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.gravitex.banking_core.dto.AccountInfo;
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.BudgetPlanningEvaluation;
import de.gravitex.banking_core.dto.TradingPartnersMergeResult;
import de.gravitex.banking_core.entity.Account;
import de.gravitex.banking_core.entity.Booking;
import de.gravitex.banking_core.entity.BookingImport;
import de.gravitex.banking_core.entity.BookingImportItem;
import de.gravitex.banking_core.entity.BudgetPlanning;
import de.gravitex.banking_core.entity.BudgetPlanningItem;
import de.gravitex.banking_core.entity.ImportType;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.entity.TradingPartner;
import de.gravitex.banking_core.exception.BudgetPlanningException;
import de.gravitex.banking_core.exception.ImportDirectoryMandatoryException;
import de.gravitex.banking_core.exception.MergeTradingPartnersException;
import de.gravitex.banking_core.importer.KreisSparKasseCsvBookingImporter;
import de.gravitex.banking_core.importer.VolksbankCsvBookingImporter;
import de.gravitex.banking_core.importer.base.BookingImporter;
import de.gravitex.banking_core.repository.AccountRepository;
import de.gravitex.banking_core.repository.BookingImportItemRepository;
import de.gravitex.banking_core.repository.BookingImportRepository;
import de.gravitex.banking_core.repository.BookingRepository;
import de.gravitex.banking_core.repository.BudgetPlanningRepository;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import de.gravitex.banking_core.util.DateUtil;
import de.gravitex.banking_core.util.StringHelper;
import jakarta.transaction.Transactional;

@Service
public class BankingService {
	
	private Logger logger = LoggerFactory.getLogger(BankingService.class);

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
	
	@Autowired
	private BudgetPlanningRepository budgetPlanningRepository;	
	
	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;
	
	@Autowired
	DataIntegrityService integrityService;

	private static final Map<ImportType, BookingImporter> IMPORTERS = new HashMap<>();
	static {
		IMPORTERS.put(ImportType.CSV_VB, new VolksbankCsvBookingImporter());
		IMPORTERS.put(ImportType.CSV_KSK, new KreisSparKasseCsvBookingImporter());
	}

	public void importBookings() {
		checkImportRoot();
		logger.info("importing all bookings [" + rootDirectory + "]...");
		for (Account account : accountRepository.findAll()) {
			importBookingsForAccount(account);
		}
	}

	public List<BookingFileImportDto> importBookingsForAccount(Account account) {
		checkImportRoot();
		ImportDescriptor importDescriptor = getImportDescriptor(account);
		String importPath = importDescriptor.buildImportPath();
		logger.info("importing bookings for account [" + account + "] --> Pfad: " + importPath + " ["
				+ account.getCreditInstitute().getImportType() + "]");
		return processFiles(importPath, account, importDescriptor);
	}

	private void checkImportRoot() {
		Path path = Paths.get(rootDirectory);
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("import root {"+rootDirectory+"} does not exist!!!");
		}
	}

	@Transactional
	private List<BookingFileImportDto> processFiles(String directoryPath, Account account, ImportDescriptor importDescriptor) {
		List<BookingFileImportDto> result = new ArrayList<>();
		File directory = new File(directoryPath);
		File[] listedFiles = directory.listFiles();
		if (listedFiles != null && listedFiles.length > 0) {
			for (File file : listedFiles) {
				List<Booking> fileResult = processFile(account, file, importDescriptor);
				if (fileResult != null && !fileResult.isEmpty()) {
					BookingFileImportDto dto = new BookingFileImportDto();
					dto.setImportedBookings(fileResult);
					dto.setFileName(file.getName());
					result.add(dto);
				}
			}
		}		
		return result;
	}

	private List<Booking> processFile(Account account, File file, ImportDescriptor importDescriptor) {
		List<Booking> bookings = getImporter(account.getCreditInstitute().getImportType()).generateBookings(file, account);
		if (bookings != null && !bookings.isEmpty()) {
			List<Booking> newBookings = new ArrayList<>();
			for (Booking booking : bookings) {
				booking.setTradingPartner(getOrCreateTradingPartner(booking));
				booking.setAccount(account);
				newBookings.add(booking);
			}
			List<Booking> persistedBookings = persistNewBookings(newBookings, importDescriptor);
			if (persistedBookings == null || persistedBookings.isEmpty()) {
				logger.info("Keine neuen Umsätze in Datei[" + file.getName() + "] --> kein Import erstellt!!!");
			}
			createImport(file, account, persistedBookings);
			return persistedBookings;
		} else {
			return new ArrayList<>();
		}		
	}

	private BookingImport createImport(File aImportFile, Account account, List<Booking> aBookings) {
		BookingImport bookingImport = new BookingImport();
		bookingImport.setFileName(aImportFile.getName());
		bookingImport.setAccount(account);
		bookingImport.setImportDate(LocalDate.now());
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
			logger.info(newBookings + " new bookings imported for account --> " + account);
			logger.info(ignoredBookings + " bookings ignored (NOT imported) for account --> " + account);
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
			tmp.add(String.valueOf(DateUtil.getMilliSeconds(aBooking.getBookingDate())));
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

	public void checkForImportDirectory(Account account) {
		String importPath = new ImportDescriptor(account).buildImportPath();
		if (!isImportDirectoryPresent(importPath)) {
			throw new ImportDirectoryMandatoryException(importPath);
		}
	}

	private boolean isImportDirectoryPresent(String importPath) {
		boolean exists = Files.exists(Paths.get(importPath));
		return exists;		
	}

	public List<AccountInfo> createAccountInfo() {
		List<AccountInfo> result = new ArrayList<>();
		for (Account account : accountRepository.findAll()) {
			AccountInfo info = new AccountInfo();
			info.setAccount(account);
			info.setActualAmount(getActualAmount(account));
			result.add(info);
		}
		return result;
	}

	private BigDecimal getActualAmount(Account account) {
		
		LocalDate latestBookingDate = bookingRepository.findLatestBookingDate(account);
		List<Booking> bookings = bookingRepository.findByAccountAndBookingDateOrderByAmountAfterBookingDesc(account, latestBookingDate);
		if (bookings == null || bookings.isEmpty()) {
			return BigDecimal.ZERO;	
		}
		return bookings.get(0).getAmountAfterBooking();
	}

	public BudgetPlanningEvaluation createBudgetPlanningEvaluation(int month, int year) {
		
		BudgetPlanningEvaluation evaluation = new BudgetPlanningEvaluation();
		BudgetPlanning budgetPlanning = budgetPlanningRepository.findByPlanningYearAndPlanningMonth(year, month);		
		if (budgetPlanning == null) {
			throw new BudgetPlanningException("no budget planning provdided for {"+month+"/"+year+"}!!!");
		}
		LocalDate reference = LocalDate.of(year, month, 1);
		LocalDate startDay = reference.with(TemporalAdjusters.firstDayOfMonth());				
		LocalDate endDay = reference.with(TemporalAdjusters.lastDayOfMonth());
		List<Booking> bookingsInRange = bookingRepository.findBookingsInRange(startDay, endDay);
		logger.info(
				"evaluating {" + bookingsInRange.size() + "} bookings from {" + startDay + "} to {" + endDay + "} ...");
		for (BudgetPlanningItem aPlanningItem : budgetPlanning.getBudgetPlanningItems()) {
			evaluation.initPurposeKey(aPlanningItem.getPurposeCategory().getPurposeKey());
		}
		for (Booking bookingInRange : bookingsInRange) {
			evaluation.acceptBooking(bookingInRange);
		}
		return evaluation;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public TradingPartnersMergeResult mergeTradingPartners(List<TradingPartner> aTradingPartners, String newTradingKey) {
		
		if (aTradingPartners == null || aTradingPartners.isEmpty()) {
			throw new MergeTradingPartnersException("no trading partners provided to merge!!!");
		}
		
		if (StringHelper.isBlank(newTradingKey)) {
			throw new MergeTradingPartnersException("new trading key must be provided!!!");
		}
		
		TradingPartnersMergeResult mergeTradingPartners = new TradingPartnersMergeResult();
				
		Set<Long> existingPurposeCategoryIds = new HashSet<>();
		for (TradingPartner aTradingPartner : aTradingPartners) {
			if (aTradingPartner.getPurposeCategory() != null) {
				existingPurposeCategoryIds.add(aTradingPartner.getPurposeCategory().getId());
			}
		}
		if (existingPurposeCategoryIds.size() > 1) {
			throw new MergeTradingPartnersException("exisiting purpose categories must be unique!!!");
		}
		// all refering bookings
		List<Booking> bookingsToSwitch = new ArrayList<>();
		for (TradingPartner aTradingPartner : aTradingPartners) {			
			bookingsToSwitch.addAll((Collection<? extends Booking>) integrityService
					.satisfyPotientallyReferenced(PotientallyReferenced.forEntity(aTradingPartner)
							.withPotentiallyReferringEntity(Booking.class, "tradingPartner"))
					.getReferringEntities(Booking.class));
		}		
		// switch to new trading partner
		TradingPartner newTradingPartner = createTradingPartner(newTradingKey,
				determinePurposeCategory(existingPurposeCategoryIds));
		mergeTradingPartners.setNewTradingPartner(newTradingPartner);
		for (Booking aBookingToSwitch : bookingsToSwitch) {
			aBookingToSwitch.setTradingPartner(newTradingPartner);
			bookingRepository.save(aBookingToSwitch);
			mergeTradingPartners.addSwitchedBooking(aBookingToSwitch);
		}
		// remove given trading partners
		for (TradingPartner aTradingPartner : aTradingPartners) {
			tradingPartnerRepository.delete(aTradingPartner);
		}
				
		return mergeTradingPartners;
	}

	private PurposeCategory determinePurposeCategory(Set<Long> existingPurposeCategoryIds) {
		if (existingPurposeCategoryIds.isEmpty()) {
			return null;
		}
		return purposeCategoryRepository.findById(new ArrayList<>(existingPurposeCategoryIds).get(0)).get();
	}

	private TradingPartner createTradingPartner(String newTradingKey, PurposeCategory aPurposeCategory) {
		TradingPartner newTradingPartner = new TradingPartner();
		newTradingPartner.setTradingKey(newTradingKey);
		newTradingPartner.setPurposeCategory(aPurposeCategory);
		return tradingPartnerRepository.save(newTradingPartner);
	}
}