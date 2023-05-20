package com.example.projectboard.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidateInputDataException extends AuthenticationException {
    public InvalidateInputDataException(String msg) {
        super(msg);
    }
}
