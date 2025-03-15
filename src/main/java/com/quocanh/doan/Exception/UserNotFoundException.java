package com.quocanh.doan.Exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserNotFoundException extends ExceptionClass {
    public UserNotFoundException(String message) {
            super(message);
    }
    public UserNotFoundException(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
            super(title, status, errors, instance);
    }
}