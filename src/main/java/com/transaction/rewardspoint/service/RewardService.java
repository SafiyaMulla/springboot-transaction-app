package com.transaction.rewardspoint.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transaction.rewardspoint.exception.TransactionNotFoundException;
import com.transaction.rewardspoint.model.Transaction;
import com.transaction.rewardspoint.repository.TransactionRepository;

@Service
public class RewardService {

	@Autowired
	private TransactionRepository transactionRepository;

	public Map<Month, Integer> getRewardsPerMonth(String customerId, int year) throws TransactionNotFoundException {

		Map<Month, Integer> rewardsPerMonth = new EnumMap<>(Month.class);
		for (Month month : Month.values()) {
			rewardsPerMonth.put(month, 0);
		}

		LocalDate startDate = LocalDate.of(year, 1, 1);
		LocalDate endDate = LocalDate.of(year, 12, 31);

		List<Transaction> transactions = transactionRepository.findByCustIdAndTransactionDateBetween(customerId,
				startDate, endDate);
		if (transactions.isEmpty()) {
			throw new TransactionNotFoundException(
					"No transactions found for customer " + customerId + " for this " + year + " year");
		}

		Map<Month, Integer> calculatedRewards = transactions.stream()
				.collect(Collectors.groupingBy(transaction -> transaction.getTransactionDate().getMonth(),
						Collectors.summingInt(this::calculateRewardPoints)));

		calculatedRewards.forEach(rewardsPerMonth::put);

		return rewardsPerMonth;
	}

	private int calculateRewardPoints(Transaction transaction) {
		double amount = transaction.getAmount();
		int points = 0;
		if (amount > 100) {
			points = (int) (amount - 100) * 2 + 50;
		} else {
			points = (int) (amount - 50);
		}
		return points;
	}

	public int getTotalRewards(String customerId, int year) throws TransactionNotFoundException {

		return getRewardsPerMonth(customerId, year).values().stream().mapToInt(Integer::intValue).sum();

	}

}
