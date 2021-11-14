package com.thoughtworks.auction.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INSUFFICIENT_BALANCE(1001, "transaction failed, because of insufficient balance"),

    PAYMENT_SERVICE_UNAVAILABLE(1002, "payment service unavailable, please retry later");

    private final int code;

    private final String msg;
}
