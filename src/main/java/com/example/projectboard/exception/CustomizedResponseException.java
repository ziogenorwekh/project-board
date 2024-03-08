package com.example.projectboard.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;


public class CustomizedResponseException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errMessage;

    public CustomizedResponseException(HttpStatus httpStatus,String errMessage) {
        this.httpStatus = httpStatus;
        this.errMessage = errMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return this.errMessage;
    }
}
