package com.api.transactions.serviceimpl;

import com.api.transactions.data.cassandra.repository.TransactionRepository;
import com.api.transactions.model.GetResponse;
import com.api.transactions.model.Transactions;
import com.api.transactions.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final static String DEPOSIT = "DEPOSIT";

    private final static String WITHDRAW = "WITHDRAW";

    private final static String errorMessage = "No Transactions Found For Given Account Number";


    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseEntity<?> getTransactions(String accountNumber, LocalDate startDate, LocalDate endDate, String type) {
        Date beforeDate = null;
        Date afterDate = null;
        if (startDate != null && endDate != null) {
            beforeDate = java.sql.Date.valueOf(startDate);
            afterDate = java.sql.Date.valueOf(endDate);
        }

        List<Transactions> transactionsList = transactionRepository.findAllByAccountNumber(accountNumber);
        if (transactionsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundError());
        }
        if (startDate != null && endDate != null && (DEPOSIT.equalsIgnoreCase(type) || WITHDRAW.equalsIgnoreCase(type))) {
            return ResponseEntity.ok(filterRecordsBasedOnallParams(beforeDate, afterDate, type, transactionsList));
        } else if (startDate != null && endDate != null) {
            return ResponseEntity.ok(filterRecordsBasedOnDates(beforeDate, afterDate, transactionsList));
        } else if (DEPOSIT.equalsIgnoreCase(type) || WITHDRAW.equalsIgnoreCase(type)) {
            return ResponseEntity.ok(filterRecordsBasedOnType(transactionsList, type));
        }
        return ResponseEntity.ok(transactionsList);
    }

    private List<Transactions> filterRecordsBasedOnType(List<Transactions> transactionsList, String type) {
        List<Transactions> response = new ArrayList<>();
        transactionsList.forEach(it ->
        {
            if (it.getType().equalsIgnoreCase(type)) {
                response.add(it);
            }
        });
        return response;
    }

    private List<Transactions> filterRecordsBasedOnDates(Date startDate, Date endDate, List<Transactions> transactionsList) {
        List<Transactions> response = new ArrayList<>();
        transactionsList.forEach(it ->
        {
            if (it.getTransactionTs().after(startDate) && it.getTransactionTs().before(endDate)) {
                response.add(it);
            }
        });
        return response;
    }

    private List<Transactions> filterRecordsBasedOnallParams(Date startDate, Date endDate, String type, List<Transactions> transactionsList) {

        List<Transactions> response = new ArrayList<>();
        transactionsList.forEach(it ->
        {
            if (it.getTransactionTs().after(startDate) && it.getTransactionTs().before(endDate) && it.getType().equalsIgnoreCase(type)) {
                response.add(it);
            }
        });
        return response;
    }

    private Date stringToDate(String date) {
        if (null != date && !date.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date dateAfter = null;
            try {
                dateAfter = formatter.parse(date);
            } catch (ParseException e) {
            }
            return dateAfter;
        }
        return null;
    }

    private GetResponse notFoundError() {
        return new GetResponse(errorMessage, 404);
    }
}
