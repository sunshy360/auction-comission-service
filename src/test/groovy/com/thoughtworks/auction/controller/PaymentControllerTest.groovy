package com.thoughtworks.auction.controller

import com.thoughtworks.auction.service.PaymentService
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
            paymentService.payForTransaction(1L) >> PayStatus.SUCCESS
        expect:
            mockMvc.perform(post("/orders/1/transaction-payment")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.data', is("SUCCESS")))
    }
}
