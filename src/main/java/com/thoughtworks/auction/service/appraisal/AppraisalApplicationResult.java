package com.thoughtworks.auction.service.appraisal;

import com.thoughtworks.auction.controller.ErrorCode;
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

    private ErrorCode errorCode;
}
