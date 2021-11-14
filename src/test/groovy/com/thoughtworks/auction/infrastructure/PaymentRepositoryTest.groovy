package com.thoughtworks.auction.infrastructure

import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DataJpaTest
@ActiveProfiles('test')
class PaymentRepositoryTest extends Specification {
    @Autowired
    TestEntityManager testEntityManager

    @Autowired
    AuctionCommissionOrderRepository orderRepository

    def "Should return order entity when query by id"() {
        given:
            testEntityManager.persist(new AuctionCommissionOrder(bankAccount: "0002", transactionAmount: new BigDecimal(200000.00)))
        when:
            def result = orderRepository.getOne(1L)
        then:
            result.id == 1L
            result.bankAccount == '0002'
            result.transactionAmount == new BigDecimal(200000.00)
    }

    def "Should return latest pay status for order entity when update pay status"() {
        given:
            testEntityManager.persist(new AuctionCommissionOrder(bankAccount: "0002", transactionAmount: new BigDecimal(200000.00)))
            def oldOrder = orderRepository.getOne(2L)
            oldOrder.setTransactionPaid(true)
        when:
            orderRepository.save(oldOrder)
            def newOrder = orderRepository.getOne(2L)
        then:
            newOrder.id == 2L
            newOrder.bankAccount == '0002'
            newOrder.transactionAmount == new BigDecimal(200000.00)
            newOrder.isTransactionPaid()
    }

}
