package com.thoughtworks.auction.service.payment

import com.thoughtworks.auction.controller.ErrorCode
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository
import com.thoughtworks.auction.infrastructure.payment.PaymentFeignClient
import com.thoughtworks.auction.infrastructure.payment.PaymentResponse
import com.thoughtworks.auction.infrastructure.payment.TransferRequest
import com.thoughtworks.auction.service.payment.PayStatus
import com.thoughtworks.auction.service.payment.PaymentService
import feign.FeignException
import feign.Request
import feign.RequestTemplate
import spock.lang.Specification

class PaymentServiceTest extends Specification {
    PaymentService paymentService

    PaymentFeignClient paymentFeignClient = Mock()

    AuctionCommissionOrderRepository commissionOrderRepository = Mock()

    void setup() {
        paymentService = new PaymentService(paymentFeignClient, commissionOrderRepository)
    }

    def "Should return pay success status when feign client return success"() {
        given:
            def oldOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", transactionAmount: new BigDecimal(200000.00))
            def newOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", transactionAmount: new BigDecimal(200000.00), isTransactionPaid: true)
            commissionOrderRepository.getOne(1L) >> oldOrder
            paymentFeignClient.transfer(new TransferRequest("0001", "0002", new BigDecimal(200000.00))) >> new PaymentResponse(true, null)
            commissionOrderRepository.save(newOrder) >> newOrder
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
            commissionOrderRepository.getOne(2L) >> oldOrder
            Request feignRequest = Request.create(Request.HttpMethod.POST, '/api', Map.of(), null, new RequestTemplate())
            paymentFeignClient.transfer(new TransferRequest("0001", "0003", new BigDecimal(800000.00))) >> { throw new FeignException.Conflict("insufficient balance", feignRequest, null) }
            commissionOrderRepository.save(newOrder) >> newOrder
        when:
            def result = paymentService.payForTransaction(2L)
        then:
            result.getPayStatus() == PayStatus.FAILED
            result.getErrorCode() == ErrorCode.INSUFFICIENT_BALANCE
    }

    def "Should return pay failed status when feign client timeout"() {
        given:
            def oldOrder = new AuctionCommissionOrder(id: 3, bankAccount: "0004", transactionAmount: new BigDecimal(200000.00))
            def newOrder = new AuctionCommissionOrder(id: 3, bankAccount: "0004", transactionAmount: new BigDecimal(200000.00), isTransactionPaid: false)
            commissionOrderRepository.getOne(3L) >> oldOrder
            Request feignRequest = Request.create(Request.HttpMethod.POST, '/api', Map.of(), null, new RequestTemplate())
            paymentFeignClient.transfer(new TransferRequest("0001", "0004", new BigDecimal(200000.00))) >> { throw new FeignException.ServiceUnavailable("service unavailable", feignRequest, null) }
            commissionOrderRepository.save(newOrder) >> newOrder
        when:
            def result = paymentService.payForTransaction(3L)
        then:
            result.getPayStatus() == PayStatus.FAILED
            result.getErrorCode() == ErrorCode.PAYMENT_SERVICE_UNAVAILABLE
    }
}
