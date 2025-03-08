package com.quocanh.doan.Exception.Signup;

import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.ExceptionClass;
import org.springframework.http.HttpStatus;

import java.util.List;

public class EmailExeption extends ExceptionClass {
    public EmailExeption(String message) {
        super(message);
    }
    public EmailExeption(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
        super(title, status, errors, instance);
    }
}
