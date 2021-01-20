package com.api.transactions.service;

import com.api.transactions.model.Transactions;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    ResponseEntity<?> getTransactions(String accountNumber, LocalDate startDate, LocalDate endDate, String type);
}
