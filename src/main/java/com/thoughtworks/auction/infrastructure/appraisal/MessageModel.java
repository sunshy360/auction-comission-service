package com.thoughtworks.auction.infrastructure.appraisal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    @NotNull
    private Long commissionOrderId;

    @NotNull
    private Long auctionItemId;

    @NotBlank
    private String auctionName;

    @NotBlank
    private String auctionType;

    private String description;
}
