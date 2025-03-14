package com.quocanh.doan.Exception.CheckCode;

import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.ExceptionClass;
import org.springframework.http.HttpStatus;

import java.util.List;

public class EmailVerifycationException extends ExceptionClass {
    public EmailVerifycationException(String message) {
        super(message);
    }
    public EmailVerifycationException(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
        super(title, status, errors, instance);
    }
}