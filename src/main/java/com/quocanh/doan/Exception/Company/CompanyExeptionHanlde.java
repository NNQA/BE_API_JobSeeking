package com.quocanh.doan.Exception.Company;

public class CompanyExeptionHanlde extends RuntimeException{
    public CompanyExeptionHanlde() {
        super();
    }
    public CompanyExeptionHanlde(String message) {
        super(message);
    }


    public CompanyExeptionHanlde(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyExeptionHanlde(Throwable cause) {
        super(cause);
    }
}
