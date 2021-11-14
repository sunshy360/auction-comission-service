package com.thoughtworks.auction.infrastructure.appraisal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    private Long commissionOrderId;

    private Long auctionItemId;

    private String auctionName;

    private String auctionType;
}
