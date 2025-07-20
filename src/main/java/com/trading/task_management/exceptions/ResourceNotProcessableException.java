package com.trading.task_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceNotProcessableException extends RuntimeException{
    public ResourceNotProcessableException(String msg) {
        super(msg);
    }
}

//The request is well-formed not wrong or not bad req, but the operation cannot be performed
// due to logical constraints (422) which mean that the type you send is not supported or wrong logic