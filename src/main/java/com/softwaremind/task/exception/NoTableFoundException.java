package com.softwaremind.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class NoTableFoundException extends ErrorResponseException {
    public NoTableFoundException() {
        super(HttpStatus.CONFLICT);
        setDetail("No available tables found");
    }
}
