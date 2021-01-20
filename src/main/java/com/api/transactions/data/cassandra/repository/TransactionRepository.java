package com.api.transactions.data.cassandra.repository;

import com.api.transactions.model.Transactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TransactionRepository extends CrudRepository<Transactions, String> {

    List<Transactions> findAllByAccountNumber(String accountNumber);
}
