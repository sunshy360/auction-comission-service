package com.thoughtworks.auction.controller

import com.thoughtworks.auction.service.payment.PayStatus
import com.thoughtworks.auction.service.payment.PayResult
import com.thoughtworks.auction.service.payment.PaymentService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = PaymentController)
class PaymentControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpringBean
    PaymentService paymentService = Mock()

    def "Should return pay success when auction company account balance is greater than transaction amount"() {
        given:
            paymentService.payForTransaction(1L) >> new PayResult(PayStatus.SUCCESS, null)
        expect:
            mockMvc.perform(post("/orders/1/transaction-payment/confirmation")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.data', is("SUCCESS")))
    }

    def "Should return pay failed when auction company account balance is insufficient"() {
        given:
            paymentService.payForTransaction(2L) >> new PayResult(PayStatus.FAILED, ErrorCode.INSUFFICIENT_BALANCE)
        expect:
            mockMvc.perform(post("/orders/2/transaction-payment/confirmation")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath('$.error.code', is(1001)))
                    .andExpect(jsonPath('$.error.message', is("transaction failed, because of insufficient balance")))
    }

    def "Should return pay failed when payment service is down"() {
        given:
            paymentService.payForTransaction(3L) >> new PayResult(PayStatus.FAILED, ErrorCode.PAYMENT_SERVICE_UNAVAILABLE)
        expect:
            mockMvc.perform(post("/orders/3/transaction-payment/confirmation")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isServiceUnavailable())
                    .andExpect(jsonPath('$.error.code', is(1002)))
                    .andExpect(jsonPath('$.error.message', is("payment service unavailable, please retry later")))
    }
}
