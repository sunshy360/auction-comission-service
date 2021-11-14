package com.thoughtworks.auction.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INSUFFICIENT_BALANCE(1001, "transaction failed, because of insufficient balance", "insufficient balance", HttpStatus.CONFLICT),

    PAYMENT_SERVICE_UNAVAILABLE(1002, "payment service unavailable, please retry later", "service unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    private final int code;

    private final String msg;

    private final String feignMessage;

    private final HttpStatus httpStatus;
}
