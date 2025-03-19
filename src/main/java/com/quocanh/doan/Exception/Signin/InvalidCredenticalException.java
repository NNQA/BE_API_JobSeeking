package com.quocanh.doan.Exception.Signin;

import com.quocanh.doan.Exception.BadRequetException;
import com.quocanh.doan.Exception.ErrorDetail;
import com.quocanh.doan.Exception.ExceptionClass;
import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidCredenticalException extends ExceptionClass {
    public InvalidCredenticalException(String message) {
        super(message);
    }
    public InvalidCredenticalException(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
        super(title, status, errors, instance);
    }
}