package com.trading.task_management.exceptions;

public class EmailSendFailureException extends RuntimeException {

    public EmailSendFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}

