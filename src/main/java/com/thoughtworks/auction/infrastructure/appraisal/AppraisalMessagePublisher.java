package com.thoughtworks.auction.infrastructure.appraisal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppraisalMessagePublisher {
    public static final String APPRAISAL_EXCHANGE_NAME = "appraisal.fanout";

    public static final String DEFAULT_ROUTING_KEY_PREFIX = "simple.topic";

    private final AmqpTemplate rabbitTemplate;

    public AppraisalApplicationResponse publishAppraisalApplication(Long orderId, Long auctionItemId, String name, String type, String description) {
        try {
            MessageModel messageModel = MessageModel.builder().commissionOrderId(orderId).auctionItemId(auctionItemId).auctionName(name).auctionType(type).description(description).build();
            validateMessageModel(messageModel);
            rabbitTemplate.convertAndSend(APPRAISAL_EXCHANGE_NAME, DEFAULT_ROUTING_KEY_PREFIX, messageModel);
            return AppraisalApplicationResponse.builder().success(true).build();
        } catch (Exception e) {
            log.error("publish appraisal application message with error", e);
            return AppraisalApplicationResponse.builder().success(false).failedReason(e.getMessage()).build();
        }
    }

    private void validateMessageModel(MessageModel messageModel) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<MessageModel>> validateResults = validator.validate(messageModel);
        String validationErrorMsg = validateResults.stream().map(ConstraintViolation::getMessage).sorted().collect(Collectors.joining(";"));
        if (!validationErrorMsg.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
