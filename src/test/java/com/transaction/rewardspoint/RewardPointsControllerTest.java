package com.transaction.rewardspoint;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.transaction.rewardspoint.controller.RewardPointsController;
import com.transaction.rewardspoint.exception.TransactionNotFoundException;
import com.transaction.rewardspoint.repository.TransactionRepository;
import com.transaction.rewardspoint.service.RewardService;

public class RewardPointsControllerTest {

	@Mock
	private RewardService rewardService;
	@Mock
	private TransactionRepository repo;
	@InjectMocks
	private RewardPointsController rewardsController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCustomerRewards() throws TransactionNotFoundException {
		Map<Month, Integer> rewards = new EnumMap<>(Month.class);
		rewards.put(Month.AUGUST, 90);
		rewards.put(Month.SEPTEMBER, 30);

		when(rewardService.getRewardsPerMonth(anyString(), anyInt())).thenReturn(rewards);

		Map<Month, Integer> response = rewardsController.getCustomerRewardsByMonthly("cust1", 2024);

		assertNotNull(response);
		Assertions.assertEquals(90, response.get(Month.AUGUST));
		Assertions.assertEquals(30, response.get(Month.SEPTEMBER));
	}

	@Test
	public void testGetTotalRewards() throws TransactionNotFoundException {
		when(rewardService.getTotalRewards(anyString(), anyInt())).thenReturn(120);

		Object totalRewards = rewardsController.getTotalRewards("cust1", 2024);
		Object total = "Total Rewards: " + 120;

		Assertions.assertEquals(totalRewards, total);
	}

	@Test
	public void testGetRewardsPerMonth_NoTransactionsFound() throws TransactionNotFoundException {
		String customerId = "";
		int year = 2023;

		when(repo.findByCustIdAndTransactionDateBetween(customerId, LocalDate.of(year, 1, 1),
				LocalDate.of(year, 12, 31))).thenReturn(List.of());
		when(rewardService.getRewardsPerMonth(anyString(), anyInt())).thenThrow(TransactionNotFoundException.class);

	}

	@Test
	public void testGetTotalRewards_NoTransactionsFound() throws TransactionNotFoundException {
		String customerId = "";
		int year = 2023;
		when(repo.findByCustIdAndTransactionDateBetween(customerId, LocalDate.of(year, 1, 1),
				LocalDate.of(year, 12, 31))).thenReturn(List.of());
		when(rewardService.getTotalRewards(anyString(), anyInt())).thenThrow(TransactionNotFoundException.class);

	}
}