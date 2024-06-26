package com.quocanh.doan.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequetException extends RuntimeException{
    public BadRequetException() {
        super();
    }
    public BadRequetException(String message) {
        super(message);
    }


    public BadRequetException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequetException(Throwable cause) {
        super(cause);
    }
}
