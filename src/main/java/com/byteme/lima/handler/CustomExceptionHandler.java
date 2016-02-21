package com.byteme.lima.handler;

import com.byteme.lima.domain.CustomResponse;
import com.byteme.lima.exception.NotFoundException;
import com.byteme.lima.exception.TeapotException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
@ResponseBody
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomExceptionHandler {
    public static final Log LOG = LogFactory.getLog(CustomExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomResponse handle404(HttpServletRequest request, Throwable throwable) {
        return this.handle(HttpStatus.NOT_FOUND, request, throwable, true);
    }

    @ExceptionHandler(TeapotException.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public CustomResponse handle418(HttpServletRequest request, Throwable throwable) {
        return this.handle(HttpStatus.I_AM_A_TEAPOT, request, throwable, false);
    }

    @ExceptionHandler(
            value = {
                    IOException.class,
                    Exception.class
            }
    )
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomResponse handle500(HttpServletRequest request, Throwable throwable) {
        return this.handle(HttpStatus.INTERNAL_SERVER_ERROR, request, throwable, true);
    }

    private CustomResponse handle(HttpStatus status, HttpServletRequest request, Throwable throwable, boolean withStackFrames) {
        LOG.error(throwable.getMessage(), throwable);

        return CustomResponse.builder()
                .uri(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .http(CustomResponse.HttpStatusInfo.builder()
                        .status(status.value())
                        .reason(status.getReasonPhrase())
                        .build()
                )
                .exception(CustomResponse.ExceptionInfo.builder()
                        .type(throwable.getClass())
                        .message(throwable.getMessage())
                        .stackFrames(withStackFrames
                                ? ExceptionUtils.getStackFrames(throwable)
                                : new String[] {"hidden"}
                        )
                        .build()
                )
                .build();
    }
}