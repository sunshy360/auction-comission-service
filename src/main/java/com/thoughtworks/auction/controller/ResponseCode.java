package com.thoughtworks.auction.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    PAY_SUCCESS(1000, "支付成功");

    private final int code;

    private final String msg;
}
