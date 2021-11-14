package com.thoughtworks.auction.service;

import com.thoughtworks.auction.controller.ErrorCode;
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrderRepository;
import com.thoughtworks.auction.infrastructure.PaymentFeignClient;
import com.thoughtworks.auction.infrastructure.PaymentResponse;
import com.thoughtworks.auction.infrastructure.TransferRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String AUCTION_COMPANY_ACCOUNT = "0001";

    public static final String PAYMENT_SERVICE_UNAVAILABLE_REASON = "payment service unavailable, please retry later";

    private final PaymentFeignClient paymentFeignClient;

    private final AuctionCommissionOrderRepository paymentRepository;

    public PayResult payForTransaction(Long orderId) {
        AuctionCommissionOrder order = paymentRepository.getOne(orderId);
        PaymentResponse paymentResponse = doPay(order);
        order.setTransactionPaid(paymentResponse.isSuccess());
        AuctionCommissionOrder savedOrder = paymentRepository.save(order);
        return savedOrder.isTransactionPaid() ? PayResult.builder().payStatus(PayStatus.SUCCESS).build() :
                PayResult.builder().payStatus(PayStatus.FAILED).errorCode(buildErrorCode(paymentResponse.getFailedReason())).build();
    }

    private PaymentResponse doPay(AuctionCommissionOrder order) {
        try {
            return paymentFeignClient.transfer(buildTransferRequest(order));
        } catch (FeignException.ServiceUnavailable e) {
            log.error("service unavailable", e);
            return PaymentResponse.builder().success(false).failedReason(PAYMENT_SERVICE_UNAVAILABLE_REASON).build();
        }
    }

    private ErrorCode buildErrorCode(String failedReason) {
        return Arrays.stream(ErrorCode.values()).filter(e -> e.getMsg().equals(failedReason)).findFirst().orElseThrow();
    }

    private TransferRequest buildTransferRequest(AuctionCommissionOrder order) {
        return TransferRequest.builder()
                .payerAccount(AUCTION_COMPANY_ACCOUNT)
                .receiverAccount(order.getBankAccount())
                .totalAmount(order.getTransactionAmount())
                .build();
    }
}
