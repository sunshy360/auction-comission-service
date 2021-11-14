package com.thoughtworks.auction.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("payment-service")
public interface PaymentFeignClient {
    @PostMapping("/alipay/transfer")
    PaymentResponse transfer(TransferRequest request);
}