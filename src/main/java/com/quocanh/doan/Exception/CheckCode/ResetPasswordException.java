package com.quocanh.doan.Exception.CheckCode;

import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.ExceptionClass;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ResetPasswordException extends ExceptionClass {
    public ResetPasswordException(String message) {
        super(message);
    }
    public ResetPasswordException(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
        super(title, status, errors, instance);
    }
}
