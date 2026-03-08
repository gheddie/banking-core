package de.gravitex.banking_core.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gravitex.banking_core.dto.BudgetPlanningDto;
import de.gravitex.banking_core.dto.BudgetPlanningItemDto;
import de.gravitex.banking_core.entity.BudgetPlanning;
import de.gravitex.banking_core.entity.BudgetPlanningItem;
import de.gravitex.banking_core.entity.PurposeCategory;
import de.gravitex.banking_core.exception.BudgetPlanningException;
import de.gravitex.banking_core.repository.BudgetPlanningItemRepository;
import de.gravitex.banking_core.repository.BudgetPlanningRepository;
import de.gravitex.banking_core.repository.PurposeCategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class BudgetPlanningService {

	@Autowired
	private BudgetPlanningRepository budgetPlanningRepository;

	@Autowired
	private BudgetPlanningItemRepository budgetPlanningItemRepository;

	@Autowired
	private PurposeCategoryRepository purposeCategoryRepository;

	@Transactional
	public void processBudgetPlanning(BudgetPlanningDto aBudgetPlanningDto) {

		assertBudgetPlanningUniqueByTimeKey(aBudgetPlanningDto);
		assertPurposeCategorysUnique(aBudgetPlanningDto);
		assertBudgetPlanningMonth(aBudgetPlanningDto);
		// assertInFuture(aBudgetPlanningDto);

		BudgetPlanning budgetPlanning = new BudgetPlanning();
		budgetPlanning.setMonth(aBudgetPlanningDto.getMonth());
		budgetPlanning.setYear(aBudgetPlanningDto.getYear());
		
		budgetPlanningRepository.save(budgetPlanning);

		for (BudgetPlanningItemDto aBudgetPlanningItemDto : aBudgetPlanningDto.getItems()) {
			BudgetPlanningItem item = new BudgetPlanningItem();
			item.setAmount(aBudgetPlanningItemDto.getQuantity());
			item.setBudgetPlanning(budgetPlanning);
			item.setPurposeCategory(findPurposeCategory(aBudgetPlanningItemDto.getPurposeCategory()));
			budgetPlanningItemRepository.save(item);
		}
	}

	private void assertInFuture(BudgetPlanningDto aBudgetPlanningDto) {
		
		LocalDate dateOfBudgetPlanning = LocalDate.of(aBudgetPlanningDto.getYear(), aBudgetPlanningDto.getMonth(), 1);
		if (!(dateOfBudgetPlanning.isAfter(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())))) {
			throw new BudgetPlanningException(
					"can not create budget planning for " + aBudgetPlanningDto.buildTimeKey() + " (must be for a point of time in future)!!!");
		}
	}

	private void assertBudgetPlanningMonth(BudgetPlanningDto aBudgetPlanningDto) {
		if (!aBudgetPlanningDto.hasValidMonth()) {
			throw new BudgetPlanningException(
					"not a valid time and month for a budget planning {" + aBudgetPlanningDto.buildTimeKey() + "}!!!");
		}
	}

	private void assertPurposeCategorysUnique(BudgetPlanningDto aBudgetPlanningDto) {
		Set<String> aPurposeKeys = new HashSet<>();
		for (BudgetPlanningItemDto aBudgetPlanningItemDto : aBudgetPlanningDto.getItems()) {
			String aPurposeCategory = aBudgetPlanningItemDto.getPurposeCategory();
			if (aPurposeKeys.contains(aPurposeCategory)) {
				throw new BudgetPlanningException("purpose category {" + aPurposeCategory
						+ "} not unique in budget planning for {" + aBudgetPlanningDto.buildTimeKey() + "}!!!");
			}
			aPurposeKeys.add(aPurposeCategory);
		}
	}

	private void assertBudgetPlanningUniqueByTimeKey(BudgetPlanningDto aBudgetPlanningDto) {
		BudgetPlanning byYearAndMonth = budgetPlanningRepository.findByYearAndMonth(aBudgetPlanningDto.getYear(),
				aBudgetPlanningDto.getMonth());
		if (byYearAndMonth != null) {
			throw new BudgetPlanningException(
					"budget planning already present for {" + aBudgetPlanningDto.buildTimeKey() + "}!!!");
		}
	}

	private PurposeCategory findPurposeCategory(String aPurposeCategory) {
		PurposeCategory purposeCategory = purposeCategoryRepository.findByPurposeKey(aPurposeCategory);
		if (purposeCategory == null) {
			throw new BudgetPlanningException("purpose category not found {" + aPurposeCategory + "}!!!");
		}
		return purposeCategory;
	}
}