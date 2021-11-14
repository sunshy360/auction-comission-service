package com.thoughtworks.auction.infrastructure

import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = [ WireMockConfig.class ])
class PaymentFeignClientTest extends Specification {

    @Autowired
    WireMockServer mockPaymentService

    @Autowired
    PaymentFeignClient paymentFeignClient

    void setup() throws IOException {
        PaymentMocks.setupMockPaymentResponse(mockPaymentService)
    }

    def "Should return pay success status when feign client return success"() {
        expect:
            paymentFeignClient.transfer(new TransferRequest("0001", "0002", new BigDecimal(200000.00))).isSuccess()
    }
}
