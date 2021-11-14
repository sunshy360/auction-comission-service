package com.thoughtworks.auction.infrastructure.commission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auction_commission_order")
public class AuctionCommissionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionItemId;

    private String bankAccount;

    private BigDecimal transactionAmount;

    private boolean isTransactionPaid;

    private boolean isAppraisalApplicationSubmitted;
}
