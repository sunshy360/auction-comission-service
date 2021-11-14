package com.thoughtworks.auction.controller;

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
    CommonResponse<PayStatus> payForTransaction(@PathVariable("oid") Long orderId) {
        return new CommonResponse<>(paymentService.payForTransaction(orderId));
    }

}
