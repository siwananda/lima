package com.byteme.lima.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomResponse {
    private String uri;
    private Long timestamp;
    private HttpStatusInfo http;
    private ExceptionInfo exception;

    @Getter
    @Setter
    @Builder
    public static class HttpStatusInfo {
        private Integer status;
        private String reason;
    }

    @Getter
    @Setter
    @Builder
    public static class ExceptionInfo {
        private Class<?> type;
        private String message;
        private String[] stackFrames;
    }
}
