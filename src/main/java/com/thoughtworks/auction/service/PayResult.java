package com.thoughtworks.auction.service;

import com.thoughtworks.auction.controller.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayResult {
    private PayStatus payStatus;

    private ErrorCode errorCode;
}
