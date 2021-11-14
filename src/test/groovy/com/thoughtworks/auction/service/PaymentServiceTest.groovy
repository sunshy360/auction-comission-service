package com.thoughtworks.auction.service

import com.thoughtworks.auction.controller.ErrorCode
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
            paymentFeignClient.transfer(new TransferRequest("0001", "0002", new BigDecimal(200000.00))) >> new PaymentResponse(true, null)
            paymentRepository.save(newOrder) >> newOrder
        when:
            def result = paymentService.payForTransaction(1L)
        then:
            result.getPayStatus() == PayStatus.SUCCESS
            result.getErrorCode() == null
    }

    def "Should return pay failed status when feign client return failed"() {
        given:
            def oldOrder = new AuctionCommissionOrder(id: 2, bankAccount: "0003", transactionAmount: new BigDecimal(800000.00))
            def newOrder = new AuctionCommissionOrder(id: 2, bankAccount: "0003", transactionAmount: new BigDecimal(800000.00), isTransactionPaid: false)
            paymentRepository.getOne(2L) >> oldOrder
            paymentFeignClient.transfer(new TransferRequest("0001", "0003", new BigDecimal(800000.00))) >> new PaymentResponse(false, "transaction failed, because of insufficient balance")
            paymentRepository.save(newOrder) >> newOrder
        when:
            def result = paymentService.payForTransaction(2L)
        then:
            result.getPayStatus() == PayStatus.FAILED
            result.getErrorCode() == ErrorCode.INSUFFICIENT_BALANCE
    }
}
