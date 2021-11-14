package com.thoughtworks.auction.infrastructure.appraisal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppraisalMessagePublisher {
    public static final String APPRAISAL_EXCHANGE_NAME = "appraisal.fanout";

    public static final String DEFAULT_ROUTING_KEY_PREFIX = "simple.topic";

    private final AmqpTemplate rabbitTemplate;

    public AppraisalApplicationResponse publishAppraisalApplication(Long orderId, Long auctionItemId, String name, String type) {
        try {
            MessageModel messageModel = MessageModel.builder().commissionOrderId(orderId).auctionItemId(auctionItemId).auctionName(name).auctionType(type).build();
            rabbitTemplate.convertAndSend(APPRAISAL_EXCHANGE_NAME, DEFAULT_ROUTING_KEY_PREFIX, messageModel);
            return AppraisalApplicationResponse.builder().success(true).build();
        } catch (Exception e) {
            log.error("publish appraisal application message with error", e);
            return AppraisalApplicationResponse.builder().success(false).failedReason(e.getMessage()).build();
        }
    }
}
