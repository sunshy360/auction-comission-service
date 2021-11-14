package com.thoughtworks.auction.service.appraisal

import com.thoughtworks.auction.infrastructure.appraisal.AppraisalApplicationResponse
import com.thoughtworks.auction.infrastructure.appraisal.AppraisalMessagePublisher
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItem
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItemRepository
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository
import spock.lang.Specification

class AuctionItemAppraisalServiceTest extends Specification {
    AuctionItemAppraisalService appraisalService

    AuctionCommissionOrderRepository orderRepository = Mock()

    AuctionItemRepository auctionItemRepository = Mock()

    AppraisalMessagePublisher appraisalMessagePublisher = Mock()

    void setup() {
        appraisalService = new AuctionItemAppraisalService(orderRepository, auctionItemRepository, appraisalMessagePublisher)
    }

    def "Should return submit success status when message publisher return success"() {
        given:
            def oldOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", auctionItemId: 10L, transactionAmount: new BigDecimal(200000.00),
                    isTransactionPaid: false, isAppraisalApplicationSubmitted: false)
            def newOrder = new AuctionCommissionOrder(id: 1, bankAccount: "0002", auctionItemId: 10L, transactionAmount: new BigDecimal(200000.00),
                    isTransactionPaid: true, isAppraisalApplicationSubmitted: true)
            orderRepository.getOne(1L) >> oldOrder
            def auctionItem = new AuctionItem(id: 10L, name: "the smile of monalisa", type: "painting")
            auctionItemRepository.findById(10L) >> Optional.ofNullable(auctionItem)
            appraisalMessagePublisher.publishAppraisalApplication(1L, 10L, "the smile of monalisa", "painting") >> new AppraisalApplicationResponse(true, null)
            orderRepository.save(newOrder) >> newOrder
        when:
            def result = appraisalService.submitAppraisalApplication(1L)
        then:
            result.getStatus() == AppraisalApplicationStatus.SUBMITTED
            result.getReason() == null
    }

}
