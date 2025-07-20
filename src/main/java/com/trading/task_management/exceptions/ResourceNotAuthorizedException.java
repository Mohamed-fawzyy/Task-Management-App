package com.trading.task_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceNotAuthorizedException extends RuntimeException {
    public ResourceNotAuthorizedException(String message) {
        super(message);
    }
}