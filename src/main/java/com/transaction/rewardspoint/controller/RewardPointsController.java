package com.transaction.rewardspoint.controller;

import java.time.Month;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transaction.rewardspoint.exception.TransactionNotFoundException;
import com.transaction.rewardspoint.service.RewardService;

@RestController
@RequestMapping("/api/rewards")
public class RewardPointsController {

	@Autowired
	private RewardService rewardsService;

	@GetMapping("/monthly")
	public Map<Month, Integer> getCustomerRewardsByMonthly(@RequestParam String custId, @RequestParam int year)
			throws TransactionNotFoundException {

		return rewardsService.getRewardsPerMonth(custId, year);
	}

	@GetMapping("/yearly")
	public String getTotalRewards(@RequestParam String custId, @RequestParam int year)
			throws TransactionNotFoundException {

		return "Total Rewards: " + rewardsService.getTotalRewards(custId, year);
	}
}
