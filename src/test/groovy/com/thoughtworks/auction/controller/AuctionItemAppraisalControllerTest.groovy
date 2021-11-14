package com.thoughtworks.auction.controller

import com.thoughtworks.auction.service.appraisal.AppraisalApplicationResult
import com.thoughtworks.auction.service.appraisal.AppraisalApplicationStatus
import com.thoughtworks.auction.service.appraisal.AuctionItemAppraisalService
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

@WebMvcTest(controllers = AuctionItemAppraisalController)
class AuctionItemAppraisalControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpringBean
    AuctionItemAppraisalService appraisalService = Mock()

    def "Should return success when submit auction item appraisal application successfully"() {
        given:
            appraisalService.submitAppraisalApplication(1) >> new AppraisalApplicationResult(AppraisalApplicationStatus.SUBMITTED, null)
        expect:
            mockMvc.perform(post("/orders/1/auction-item-appraisal")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath('$.data', is("SUBMITTED")))
    }
}
