package de.gravitex.banking_core.service;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import de.gravitex.banking.entity.Account;
import de.gravitex.banking.entity.Booking;
import de.gravitex.banking.entity.BookingImport;
import de.gravitex.banking.entity.BookingImportItem;
import de.gravitex.banking.entity.BudgetPlanning;
import de.gravitex.banking.entity.BudgetPlanningItem;
import de.gravitex.banking.entity.PurposeCategory;
import de.gravitex.banking.entity.TradingPartner;
import de.gravitex.banking.entity.TradingPartnerBookingHistory;
import de.gravitex.banking.enumerated.ImportType;
import de.gravitex.banking_core.dto.AccountInfo;
import de.gravitex.banking_core.dto.BookingFileImportDto;
import de.gravitex.banking_core.dto.BookingImportSummary;
import de.gravitex.banking_core.dto.BookingOverview;
import de.gravitex.banking_core.dto.BookingOverviewTradingKey;
import de.gravitex.banking_core.dto.BookingProgress;
import de.gravitex.banking_core.dto.BudgetPlanningEvaluation;
import de.gravitex.banking_core.dto.TradingPartnersMergeResult;
import de.gravitex.banking_core.dto.UnprocessedBookingImport;
import de.gravitex.banking_core.exception.AttachRecurringPositionException;
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
import de.gravitex.banking_core.repository.TradingPartnerBookingHistoryRepository;
import de.gravitex.banking_core.repository.TradingPartnerRepository;
import de.gravitex.banking_core.repository.util.PotientallyReferenced;
import de.gravitex.banking_core.service.util.BookingProgressByTradingKey;
import de.gravitex.banking_core.service.util.ImportDescriptor;
import de.gravitex.banking_core.service.util.ShortestBookingInterval;
import de.gravitex.banking_core.util.DateUtil;
import de.gravitex.banking_core.util.StringHelper;
import de.gravitex.banking_core.util.db.DatabaseAdministrator;
import de.gravitex.banking_core.util.db.info.base.DatabaseTypeInfo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class BankingService {

	private Logger logger = LoggerFactory.getLogger(BankingService.class);

	@Value("${spring.datasource.driver.class}")
	private String databaseDriverClass;

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
	private TradingPartnerBookingHistoryRepository tradingPartnerBookingHistoryRepository;

	@Autowired
	DataIntegrityService integrityService;

	private DatabaseTypeInfo databaseInfo;

	private static final Map<ImportType, BookingImporter> IMPORTERS = new HashMap<>();
	static {
		IMPORTERS.put(ImportType.CSV_VB, new VolksbankCsvBookingImporter());
		IMPORTERS.put(ImportType.CSV_KSK, new KreisSparKasseCsvBookingImporter());
	}

	public void importBookings() {
		checkImportRoot();
		logger.info("importing all bookings [" + databaseInfo.getImportRootDirectory() + "]...");
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
		return importFiles(importPath, account, importDescriptor);
	}

	private void checkImportRoot() {
		Path path = Paths.get(databaseInfo.getImportRootDirectory());
		if (!Files.exists(path)) {
			throw new IllegalArgumentException("import root {" + databaseInfo.getImportRootDirectory() + "} does not exist!!!");
		}
	}

	@Transactional
	private List<BookingFileImportDto> importFiles(String directoryPath, Account account,
			ImportDescriptor importDescriptor) {
		/*
		List<BookingFileImportDto> result = new ArrayList<>();
		File directory = new File(directoryPath);
		File[] listedFiles = directory.listFiles();
		if (listedFiles != null && listedFiles.length > 0) {
			for (File file : listedFiles) {
				List<Booking> fileResult = importFile(account, file);
				if (fileResult != null && !fileResult.isEmpty()) {
					BookingFileImportDto dto = new BookingFileImportDto();
					dto.setImportedBookings(fileResult);
					dto.setFileName(file.getName());
					result.add(dto);
				}
			}
		}
		*/
		return new ArrayList<BookingFileImportDto>();
	}
	
	public BookingImportSummary importFile(Account account, String aImportFileName) {
		ImportDescriptor importDescriptor = getImportDescriptor(account);
		File file = importDescriptor.getImportFile(aImportFileName);
		BookingImportSummary summary = importFile(account, file);
		moveFileToProcessed(file, importDescriptor);
		return summary;
	}

	private void moveFileToProcessed(File aBookingFile, ImportDescriptor aImportDescriptor) {
		if (databaseInfo.moveProcessedImports()) {
			aBookingFile.renameTo(new File(aImportDescriptor.getProcessedFilePath() + "\\" + aBookingFile.getName()));	
		}			
	}

	public BookingImportSummary importFile(Account account, File file) {
		List<Booking> generatedBookings = getImporter(account.getCreditInstitute().getImportType()).generateBookings(file,
				account);
		BookingImportSummary summary = new BookingImportSummary();
		summary.setBookingFileName(file.getAbsolutePath());
		if (generatedBookings != null && !generatedBookings.isEmpty()) {
			List<Booking> newBookings = new ArrayList<>();
			for (Booking booking : generatedBookings) {
				booking.setTradingPartner(getOrCreateTradingPartner(booking));
				booking.setAccount(account);
				newBookings.add(booking);
			}
			HashMap<String, Booking> generatedBookingsMap = new HashMap<String, Booking>();
			for (Booking aGeneratedBooking : generatedBookings) {
				generatedBookingsMap.put(createBookingKey(aGeneratedBooking), aGeneratedBooking);
			}
			List<Booking> persistedBookings = persistNewBookings(newBookings, getImportDescriptor(account));
			for (Booking aPersistedBooking : persistedBookings) {
				generatedBookingsMap.remove(createBookingKey(aPersistedBooking));
			}
			if (persistedBookings == null || persistedBookings.isEmpty()) {
				logger.info("Keine neuen Umsätze in Datei[" + file.getName() + "] --> kein Import erstellt!!!");
			}
			createImport(file, account, persistedBookings);			
			logger.info(generatedBookingsMap.size() + " bookings of {" + generatedBookings.size() + "} if file {"
					+ file.getAbsolutePath() + "} imported...");						
			summary.setImportedBookings(persistedBookings);
			summary.setIgnoredBookings(new ArrayList<>(generatedBookingsMap.values()));
			return summary;
		} else {
			return summary;
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
		return new ImportDescriptor(account, databaseInfo.getImportRootDirectory());
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
	}

	public void checkForImportDirectory(Account account) {
		String importPath = getImportDescriptor(account).buildImportPath();
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
		List<Booking> bookings = bookingRepository.findByAccountAndBookingDateOrderByAmountAfterBookingDesc(account,
				latestBookingDate);
		if (bookings == null || bookings.isEmpty()) {
			return BigDecimal.ZERO;
		}
		return bookings.get(0).getAmountAfterBooking();
	}

	public BudgetPlanningEvaluation createBudgetPlanningEvaluation(int month, int year) {

		BudgetPlanningEvaluation evaluation = new BudgetPlanningEvaluation();
		BudgetPlanning budgetPlanning = budgetPlanningRepository.findByPlanningYearAndPlanningMonth(year, month);
		if (budgetPlanning == null) {
			throw new BudgetPlanningException("no budget planning provdided for {" + month + "/" + year + "}!!!");
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
	public TradingPartnersMergeResult mergeTradingPartners(List<TradingPartner> aTradingPartners,
			String newTradingKey) {

		if (aTradingPartners == null || aTradingPartners.isEmpty()) {
			throw new MergeTradingPartnersException("no trading partners provided to merge!!!");
		}
		
		if (!(aTradingPartners.size() > 1)) {
			throw new MergeTradingPartnersException("at least 2 trading partners must be provided to merge!!!");
		}

		if (StringHelper.isBlank(newTradingKey)) {
			throw new MergeTradingPartnersException("new trading key must be provided!!!");
		}

		TradingPartnersMergeResult mergeTradingPartners = new TradingPartnersMergeResult();

		Set<Long> existingPurposeCategoryIds = new HashSet<>();
		for (TradingPartner aTradingPartner : aTradingPartners) {
			if (aTradingPartner.getRecurringPosition() != null) {
				throw new MergeTradingPartnersException(
						"no trading partners with a recurring position set {" + aTradingPartner + "} can be merged!!!");
			}
			if (aTradingPartner.getParentTradingPartner() != null) {
				throw new MergeTradingPartnersException(
						"only top level trading partners can be merged (not a top level trading partner --> {"
								+ aTradingPartner + "})");
			}
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
			historize(aBookingToSwitch);
			aBookingToSwitch.setTradingPartner(newTradingPartner);
			bookingRepository.save(aBookingToSwitch);
			mergeTradingPartners.addSwitchedBooking(aBookingToSwitch);			
		}
		// reparent given trading partners
		for (TradingPartner aTradingPartner : aTradingPartners) {
			aTradingPartner.setParentTradingPartner(newTradingPartner);
			tradingPartnerRepository.save(aTradingPartner);
		}

		return mergeTradingPartners;
	}

	private void historize(Booking aBooking) {
		TradingPartnerBookingHistory history = new TradingPartnerBookingHistory();
		history.setBooking(aBooking);
		history.setTradingPartner(aBooking.getTradingPartner());
		tradingPartnerBookingHistoryRepository.save(history);
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

	public void checkAttachRecurringPosition(TradingPartner aTradingPartner) {
		if (aTradingPartner.getRecurringPosition() == null) {
			return;
		}
		List<Booking> bookings = bookingRepository.findByTradingPartner(aTradingPartner);
		if (bookings == null || bookings.isEmpty()) {
			return;
		}
		Map<LocalDate, List<Booking>> byDate = new HashMap<>();
		for (Booking aBooking : bookings) {
			if (aBooking.getAmount().compareTo(BigDecimal.ZERO) > 0
					&& !aTradingPartner.getRecurringPosition().getIncoming()) {
				throw new AttachRecurringPositionException(aTradingPartner,
						"unsuitable amount (" + aBooking.getAmount() + ") detected!!!");
			}
			if (aBooking.getAmount().compareTo(BigDecimal.ZERO) < 0
					&& aTradingPartner.getRecurringPosition().getIncoming()) {
				throw new AttachRecurringPositionException(aTradingPartner,
						"unsuitable amount (" + aBooking.getAmount() + ") detected!!!");
			}
			if (byDate.get(aBooking.getBookingDate()) == null) {
				byDate.put(aBooking.getBookingDate(), new ArrayList<>());
			}
			byDate.get(aBooking.getBookingDate()).add(aBooking);
		}
		ShortestBookingInterval shortestInterval = findShortestIntervalBetweenBookingDates(byDate.keySet());
		System.out.println("shortestInterval --> " + shortestInterval);
		if (shortestInterval.getDaySpan() < aTradingPartner.getRecurringPosition().getRecurringInterval()
				.getShortestIntervalAccepted()) {
			throw new AttachRecurringPositionException(aTradingPartner, shortestInterval);
		}
	}

	private ShortestBookingInterval findShortestIntervalBetweenBookingDates(Set<LocalDate> aLocalDates) {
		List<LocalDate> datesList = new ArrayList<>(aLocalDates);
		Collections.sort(datesList);
		ShortestBookingInterval shortestInterval = new ShortestBookingInterval(Long.MAX_VALUE, null, null);
		for (int i = 0; i < datesList.size() - 1; i++) {
			LocalDate aDate1 = datesList.get(i);
			LocalDate aDate2 = datesList.get(i + 1);
			long daysBetween = getDaysBetween(aDate1, aDate2);
			System.out.println(daysBetween + " days between {" + aDate1 + "-" + aDate2 + "}...");
			if (daysBetween < shortestInterval.getDaySpan()) {
				shortestInterval = new ShortestBookingInterval(daysBetween, aDate1, aDate2);
			}
		}
		return shortestInterval;
	}

	private long getDaysBetween(LocalDate aDate1, LocalDate aDate2) {
		return ChronoUnit.DAYS.between(aDate1, aDate2);
	}

	public BookingProgress createBookingProgress(BookingProgress aBookingProgress) {
		logger.info("creating booking process (" + aBookingProgress.getStartDate() + "-" + aBookingProgress.getEndDate()
				+ ") for {" + aBookingProgress.getTradingPartners().size() + "} trading partners...");
		List<Booking> bookingsInRange = bookingRepository.findBookingsInRange(aBookingProgress.getStartDate(), aBookingProgress.getEndDate());
		Map<String, List<Booking>> mappedByTradingKey = new HashMap<>();
		for (Booking aBookingInRange : bookingsInRange) {
			String tradingKey = aBookingInRange.getTradingPartner().getTradingKey();
			if (mappedByTradingKey.get(tradingKey) == null) {
				mappedByTradingKey.put(tradingKey, new ArrayList<>());
			}
			mappedByTradingKey.get(tradingKey).add(aBookingInRange);
		}
		List<BookingProgressByTradingKey> byTradingKeyList = new ArrayList<>();
		for (String key : mappedByTradingKey.keySet()) {
			byTradingKeyList.add(new BookingProgressByTradingKey(key, mappedByTradingKey.get(key)));
		}
		aBookingProgress.setBookingProgressByTradingKeys(byTradingKeyList);
		return aBookingProgress;
	}

	public List<UnprocessedBookingImport> getUnprocessedBookingImports(Account account) {
		String importPath = getImportDescriptor(account).buildImportPath();
		File[] list = new File(importPath).listFiles();
		List<UnprocessedBookingImport> result = new ArrayList<>();
		for (File aFile : list) {
			if (aFile.isDirectory()) {
				continue;
			}
			UnprocessedBookingImport unprocessed = new UnprocessedBookingImport();
			unprocessed.setBookingFileName(aFile.getName());
			result.add(unprocessed);
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
	
    @PostConstruct
    private void postConstruct() {    	
		databaseInfo = new DatabaseAdministrator().getDatabaseInfoForDriverClass(databaseDriverClass);
    }

	public BookingOverview createBookingOverview(Account account, LocalDate aFromDate, LocalDate aUntilDate) {
		
		BookingOverview bookingOverview = new BookingOverview();
		List<Booking> bookings = bookingRepository.findBookingsByAccountInRange(aFromDate, aUntilDate, account);
		logger.info("found {" + bookings.size() + "} bookings for creating overview for account {" + account.getName()
				+ "} in range {" + aFromDate + "-" + aUntilDate + "}...");
		Map<String, BookingOverviewTradingKey> bookingsByTradingKey = new HashMap<>();
		for (Booking aBooking : bookings) {
			String tradingKey = aBooking.getTradingPartner().getTradingKey();
			if (bookingsByTradingKey.get(tradingKey) == null) {
				BookingOverviewTradingKey bot = new BookingOverviewTradingKey();
				bot.setTradingKey(tradingKey);
				bookingsByTradingKey.put(tradingKey, bot);
			}
			bookingsByTradingKey.get(tradingKey).addBooking(aBooking);
		}		
		List<BookingOverviewTradingKey> list = new ArrayList<>();
		for (String tradingKey : bookingsByTradingKey.keySet()) {
			list.add(bookingsByTradingKey.get(tradingKey).finish());
		}
		bookingOverview.setBookingOverviewTradingKeys(list);		
		return bookingOverview;
	}
}