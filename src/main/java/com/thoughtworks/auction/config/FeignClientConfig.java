package com.thoughtworks.auction.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.thoughtworks.*")
public class FeignClientConfig {
}
