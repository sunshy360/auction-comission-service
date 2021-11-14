package com.thoughtworks.auction.service.appraisal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppraisalApplicationResult {
    private AppraisalApplicationStatus status;

    private String reason;
}
