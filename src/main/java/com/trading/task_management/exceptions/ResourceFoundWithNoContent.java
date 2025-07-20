package com.trading.task_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ResourceFoundWithNoContent extends RuntimeException{
    public ResourceFoundWithNoContent(String msg) {
        super(msg);
    }
}
