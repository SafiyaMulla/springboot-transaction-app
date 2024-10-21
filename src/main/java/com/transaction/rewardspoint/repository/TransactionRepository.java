package com.transaction.rewardspoint.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.transaction.rewardspoint.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
	List<Transaction> findByCustIdAndTransactionDateBetween(String custId, LocalDate startDate, LocalDate endDate);
}
