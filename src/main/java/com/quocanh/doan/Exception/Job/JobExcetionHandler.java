package com.quocanh.doan.Exception.Job;

public class JobExcetionHandler extends RuntimeException{
    public JobExcetionHandler() {
        super();
    }
    public JobExcetionHandler(String message) {
        super(message);
    }


    public JobExcetionHandler(String message, Throwable cause) {
        super(message, cause);
    }

    public JobExcetionHandler(Throwable cause) {
        super(cause);
    }
}
