package com.quocanh.doan.Exception.Signup;

public class PasswordLengthException extends IllegalArgumentException {
    public PasswordLengthException(String message) {
        super(message);
    }
}