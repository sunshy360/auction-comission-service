package com.thoughtworks.auction.service.appraisal;

import com.thoughtworks.auction.controller.ErrorCode;
import com.thoughtworks.auction.infrastructure.appraisal.AppraisalApplicationResponse;
import com.thoughtworks.auction.infrastructure.appraisal.AppraisalMessagePublisher;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItem;
import com.thoughtworks.auction.infrastructure.appraisal.AuctionItemRepository;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrder;
import com.thoughtworks.auction.infrastructure.commission.AuctionCommissionOrderRepository;
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
        Optional<AuctionItem> auctionItemOptional = auctionItemRepository.findById(order.getAuctionItemId());
        if (auctionItemOptional.isEmpty()) {
            return AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.UNSUBMITTED).errorCode(ErrorCode.AUCTION_ITEM_NOT_EXISTS).build();
        }
        AppraisalApplicationResponse appraisalApplicationResponse = appraisalMessagePublisher.publishAppraisalApplication(orderId, order.getAuctionItemId(),
                auctionItemOptional.get().getName(), auctionItemOptional.get().getType(), auctionItemOptional.get().getDescription());
        updateAuctionCommissionOrder(order, appraisalApplicationResponse);
        return appraisalApplicationResponse.isSuccess() ? AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.SUBMITTED).build() :
                AppraisalApplicationResult.builder().status(AppraisalApplicationStatus.UNSUBMITTED).errorCode(getFailedReason(appraisalApplicationResponse)).build();
    }

    private ErrorCode getFailedReason(AppraisalApplicationResponse appraisalApplicationResponse) {
        return ErrorCode.from(appraisalApplicationResponse.getFailedReason());
    }

    private void updateAuctionCommissionOrder(AuctionCommissionOrder order, AppraisalApplicationResponse appraisalApplicationResponse) {
        order.setAppraisalApplicationSubmitted(appraisalApplicationResponse.isSuccess());
        auctionCommissionOrderRepository.save(order);
    }
}
