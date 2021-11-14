package com.thoughtworks.auction.infrastructure.appraisal;

import org.springframework.stereotype.Component;

@Component
public class AppraisalMessagePublisher {
    public AppraisalApplicationResponse publishAppraisalApplication(Long orderId, Long auctionItemId, String name, String type) {
        return AppraisalApplicationResponse.builder().success(true).build();
    }
}
