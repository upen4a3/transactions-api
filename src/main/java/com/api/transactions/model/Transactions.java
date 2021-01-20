package com.api.transactions.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Table(name = "transactions")
@Entity
public class Transactions {

    @Id
    @GeneratedValue
    private String id;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "LAST_UPDATED_TS")
    private Date transactionTs;

    @Column(name = "BALANCE")
    private double amount;
}
