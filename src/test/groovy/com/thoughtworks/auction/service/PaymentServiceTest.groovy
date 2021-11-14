package com.thoughtworks.auction.service

import com.thoughtworks.auction.controller.PayStatus
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrder
import com.thoughtworks.auction.infrastructure.AuctionCommissionOrderRepository
import com.thoughtworks.auction.infrastructure.PaymentFeignClient
import com.thoughtworks.auction.infrastructure.PaymentResponse
import com.thoughtworks.auction.infrastructure.TransferRequest
import spock.lang.Specification

class PaymentServiceTest extends Specification {
    PaymentService paymentService

    PaymentFeignClient paymentFeignClient = Mock()

    AuctionCommissionOrderRepository paymentRepository = Mock()

    void setup() {
        paymentService = new PaymentService(paymentFeignClient, paymentRepository)
    }

    def "Should return pay success status when feign client return success"() {
        given:
            def oldOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", transactionAmount: new BigDecimal(200000.00))
            def newOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", transactionAmount: new BigDecimal(200000.00), isTransactionPaid: true)
            paymentRepository.getOne(1L) >> oldOrder

            paymentFeignClient.transfer(new TransferRequest("0001", "0002", new BigDecimal(200000.00))) >> new PaymentResponse(true)
            paymentRepository.save(newOrder) >> newOrder
        when:
            def result = paymentService.payForTransaction(1L)
        then:
            result == PayStatus.SUCCESS
    }
}
