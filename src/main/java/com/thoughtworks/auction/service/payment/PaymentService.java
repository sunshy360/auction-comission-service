package com.thoughtworks.auction.service.payment;

import com.thoughtworks.auction.controller.ErrorCode;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository;
import com.thoughtworks.auction.infrastructure.payment.PaymentFeignClient;
import com.thoughtworks.auction.infrastructure.payment.PaymentResponse;
import com.thoughtworks.auction.infrastructure.payment.TransferRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String AUCTION_COMPANY_ACCOUNT = "0001";

    public static final String INSUFFICIENT_BALANCE_REASON = "insufficient balance";

    public static final String PAYMENT_SERVICE_UNAVAILABLE_REASON = "service unavailable";

    private final PaymentFeignClient paymentFeignClient;

    private final AuctionCommissionOrderRepository paymentRepository;

    public PayResult payForTransaction(Long orderId) {
        AuctionCommissionOrder order = paymentRepository.getOne(orderId);
        PaymentResponse paymentResponse = doPay(order);
        order.setTransactionPaid(paymentResponse.isSuccess());
        AuctionCommissionOrder savedOrder = paymentRepository.save(order);
        return savedOrder.isTransactionPaid() ? PayResult.builder().payStatus(PayStatus.SUCCESS).build() :
                PayResult.builder().payStatus(PayStatus.FAILED).errorCode(ErrorCode.from(paymentResponse.getFailedReason())).build();
    }

    private PaymentResponse doPay(AuctionCommissionOrder order) {
        try {
            return paymentFeignClient.transfer(buildTransferRequest(order));
        } catch (FeignException.Conflict e) {
            log.error("feign conflict exception", e);
            return PaymentResponse.builder().success(false).failedReason(INSUFFICIENT_BALANCE_REASON).build();
        } catch (FeignException.ServiceUnavailable e) {
            log.error("feign service unavailable", e);
            return PaymentResponse.builder().success(false).failedReason(PAYMENT_SERVICE_UNAVAILABLE_REASON).build();
        } catch (Exception e) {
            log.error("feign client throw exception with", e);
            return PaymentResponse.builder().success(false).failedReason(e.getMessage()).build();
        }
    }

    private TransferRequest buildTransferRequest(AuctionCommissionOrder order) {
        return TransferRequest.builder()
                .payerAccount(AUCTION_COMPANY_ACCOUNT)
                .receiverAccount(order.getBankAccount())
                .totalAmount(order.getTransactionAmount())
                .build();
    }
}
