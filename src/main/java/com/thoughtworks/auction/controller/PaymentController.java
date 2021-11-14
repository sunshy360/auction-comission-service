package com.thoughtworks.auction.controller;

import com.thoughtworks.auction.service.PayResult;
import com.thoughtworks.auction.service.PayStatus;
import com.thoughtworks.auction.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/{oid}/transaction-payment/confirmation")
    ResponseEntity<CommonResponse> payForTransaction(@PathVariable("oid") Long orderId) {
        PayResult payResult = paymentService.payForTransaction(orderId);
        if (payResult.getPayStatus().equals(PayStatus.SUCCESS)) {
            return new ResponseEntity<>(new CommonResponse<>(payResult.getPayStatus()), HttpStatus.OK);
        } else {
            ErrorCode errorCode = payResult.getErrorCode();
            return new ResponseEntity<>(new CommonResponse<>(errorCode, payResult.getPayStatus()), errorCode.getHttpStatus());
        }
    }

}
