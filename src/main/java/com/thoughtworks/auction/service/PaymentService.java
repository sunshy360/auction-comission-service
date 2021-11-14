package com.thoughtworks.auction.service;

import com.thoughtworks.auction.controller.PayStatus;
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrderRepository;
import com.thoughtworks.auction.infrastructure.PaymentFeignClient;
import com.thoughtworks.auction.infrastructure.PaymentResponse;
import com.thoughtworks.auction.infrastructure.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String AUCTION_COMPANY_ACCOUNT = "0001";

    private final PaymentFeignClient paymentFeignClient;

    private final AuctionCommissionOrderRepository paymentRepository;

    public PayStatus payForTransaction(Long orderId) {
        AuctionCommissionOrder order = paymentRepository.getOne(orderId);
        TransferRequest transferRequest = TransferRequest.builder()
                .payerAccount(AUCTION_COMPANY_ACCOUNT)
                .receiverAccount(order.getBankAccount())
                .totalAmount(order.getTransactionAmount())
                .build();
        PaymentResponse paymentResponse = paymentFeignClient.transfer(transferRequest);
        order.setTransactionPaid(paymentResponse.isSuccess());
        AuctionCommissionOrder savedOrder = paymentRepository.save(order);
        return savedOrder.isTransactionPaid() ? PayStatus.SUCCESS : PayStatus.FAILED;
    }
}
