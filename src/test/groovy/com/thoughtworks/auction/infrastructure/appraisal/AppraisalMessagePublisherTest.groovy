package com.thoughtworks.auction.infrastructure.appraisal

import org.springframework.amqp.core.AmqpTemplate
import spock.lang.Specification

class AppraisalMessagePublisherTest extends Specification {
    AppraisalMessagePublisher appraisalMessagePublisher

    AmqpTemplate amqpTemplate = Mock()

    void setup() {
        appraisalMessagePublisher = new AppraisalMessagePublisher(amqpTemplate)
    }

    def "Should publish message with full appraisal message"() {
        when:
            appraisalMessagePublisher.publishAppraisalApplication(1L, 10L, "the smile of monalisa", "painting", "please response as soon as possible")
        then:
            1 * amqpTemplate.convertAndSend("appraisal.fanout", "simple.topic", {
                verifyAll(it, MessageModel) {
                    it.commissionOrderId == 1L
                    it.auctionItemId == 10L
                    it.auctionName == "the smile of monalisa"
                    it.auctionType == "painting"
                }
            })

    }

    def "Should publish message with part appraisal message"() {
        when:
            appraisalMessagePublisher.publishAppraisalApplication(1L, 10L, "the smile of monalisa", "painting", "")
        then:
            1 * amqpTemplate.convertAndSend("appraisal.fanout", "simple.topic", {
                verifyAll(it, MessageModel) {
                    it.commissionOrderId == 1L
                    it.auctionItemId == 10L
                    it.auctionName == "the smile of monalisa"
                    it.auctionType == "painting"
                }
            })

    }
}
