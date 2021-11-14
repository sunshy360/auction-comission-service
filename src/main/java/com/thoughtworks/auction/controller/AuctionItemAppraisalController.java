package com.thoughtworks.auction.controller;

import com.thoughtworks.auction.service.appraisal.AppraisalApplicationResult;
import com.thoughtworks.auction.service.appraisal.AppraisalApplicationStatus;
import com.thoughtworks.auction.service.appraisal.AuctionItemAppraisalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class AuctionItemAppraisalController {

    private final AuctionItemAppraisalService auctionItemAppraisalService;

    @PostMapping(value = "/{oid}/auction-item-appraisal")
    ResponseEntity<CommonResponse> payForTransaction(@PathVariable("oid") Long orderId) {
        AppraisalApplicationResult appraisalApplicationResult = auctionItemAppraisalService.submitAppraisalApplication(orderId);
        if (appraisalApplicationResult.getStatus().equals(AppraisalApplicationStatus.SUBMITTED)) {
            return new ResponseEntity<>(new CommonResponse<>(appraisalApplicationResult.getStatus()), HttpStatus.ACCEPTED);
        }
        return null;
    }

}
