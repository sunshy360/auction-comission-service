package com.thoughtworks.auction.infrastructure.appraisal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalApplicationResponse {
    private boolean success;

    private String failedReason;
}
