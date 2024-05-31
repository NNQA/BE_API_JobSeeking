package com.quocanh.doan.Exception.Signin;

import com.quocanh.doan.Exception.BadRequetException;

public class InvalidCredenticalException extends BadRequetException {
    public InvalidCredenticalException() {
        super();
    }
    public InvalidCredenticalException(String message) {
        super(message);
    }

}
