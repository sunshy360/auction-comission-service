package com.thoughtworks.auction.service.appraisal;

import com.thoughtworks.auction.infrastructure.appraisal.AppraisalApplicationResponse;
import com.thoughtworks.auction.infrastructure.appraisal.AppraisalMessagePublisher;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItem;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionItemAppraisalService {
    private final AuctionCommissionOrderRepository auctionCommissionOrderRepository;

    private final AuctionItemRepository auctionItemRepository;

    private final AppraisalMessagePublisher appraisalMessagePublisher;

    public AppraisalApplicationResult submitAppraisalApplication(Long orderId) {
        AuctionCommissionOrder order = auctionCommissionOrderRepository.getOne(orderId);
        Long auctionItemId = order.getAuctionItemId();
        Optional<AuctionItem> auctionItemOptional = auctionItemRepository.findById(auctionItemId);
        AuctionItem auctionItem = auctionItemOptional.orElseThrow();
        AppraisalApplicationResponse appraisalApplicationResponse = appraisalMessagePublisher.publishAppraisalApplication(orderId, auctionItemId, auctionItem.getName(), auctionItem.getType());
        return appraisalApplicationResponse.isSuccess() ? AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.SUBMITTED).build() :
                AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.UNSUBMITTED).reason(appraisalApplicationResponse.getFailedReason()).build();
    }
}
