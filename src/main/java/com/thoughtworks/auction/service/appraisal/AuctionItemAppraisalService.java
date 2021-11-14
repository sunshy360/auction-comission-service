package com.thoughtworks.auction.service.appraisal;

import com.thoughtworks.auction.infrastructure.appraisal.AppraisalApplicationResponse;
import com.thoughtworks.auction.infrastructure.appraisal.AppraisalMessagePublisher;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItem;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItemRepository;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionItemAppraisalService {
    private final AuctionCommissionOrderRepository auctionCommissionOrderRepository;

    private final AuctionItemRepository auctionItemRepository;

    private final AppraisalMessagePublisher appraisalMessagePublisher;

    public AppraisalApplicationResult submitAppraisalApplication(Long orderId) {
        AuctionCommissionOrder order = auctionCommissionOrderRepository.getOne(orderId);
        AuctionItem auctionItem = auctionItemRepository.findById(order.getAuctionItemId()).orElseThrow();
        AppraisalApplicationResponse appraisalApplicationResponse = appraisalMessagePublisher.publishAppraisalApplication(orderId, order.getAuctionItemId(),
                auctionItem.getName(), auctionItem.getType(), auctionItem.getDescription());
        updateAuctionCommissionOrder(order, appraisalApplicationResponse);
        return appraisalApplicationResponse.isSuccess() ? AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.SUBMITTED).build() :
                AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.UNSUBMITTED).reason(appraisalApplicationResponse.getFailedReason()).build();
    }

    private void updateAuctionCommissionOrder(AuctionCommissionOrder order, AppraisalApplicationResponse appraisalApplicationResponse) {
        order.setAppraisalApplicationSubmitted(appraisalApplicationResponse.isSuccess());
        auctionCommissionOrderRepository.save(order);
    }
}
