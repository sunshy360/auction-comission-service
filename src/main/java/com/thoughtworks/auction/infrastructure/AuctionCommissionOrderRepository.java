package com.thoughtworks.auction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionCommissionOrderRepository extends JpaRepository<AuctionCommissionOrder, Long> {
}
