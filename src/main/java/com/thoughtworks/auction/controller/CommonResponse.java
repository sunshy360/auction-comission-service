package com.thoughtworks.auction.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    public static final CommonResponse<Void> NO_CONTENT = new CommonResponse<>();

    private ErrorResponse error;

    private T data;

    public CommonResponse(T value) {
        data = value;
    }

    public CommonResponse(ResponseCode errorCode, T value) {
        error = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMsg())
                .detail(value)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse<T> {
        private int code;

        private String message;

        private T detail;
    }
}
