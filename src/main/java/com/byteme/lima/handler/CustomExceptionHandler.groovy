package com.byteme.lima.handler

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
@ResponseBody
@Order(Ordered.LOWEST_PRECEDENCE)
class CustomExceptionHandler {

}