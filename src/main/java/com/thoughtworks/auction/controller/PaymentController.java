package com.thoughtworks.auction.controller;

import com.thoughtworks.auction.service.PayResult;
import com.thoughtworks.auction.service.PayStatus;
import com.thoughtworks.auction.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/{oid}/transaction-payment")
    @ResponseStatus(HttpStatus.OK)
    CommonResponse payForTransaction(@PathVariable("oid") Long orderId) {
        PayResult payResult = paymentService.payForTransaction(orderId);
        return payResult.getPayStatus().equals(PayStatus.SUCCESS) ? new CommonResponse<>(payResult.getPayStatus()) :
                new CommonResponse<>(payResult.getErrorCode(), payResult.getPayStatus());
    }

}
