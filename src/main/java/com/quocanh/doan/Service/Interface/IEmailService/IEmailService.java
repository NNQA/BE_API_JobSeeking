package com.quocanh.doan.Service.Interface.IEmailService;

public interface IEmailService {
    void sendMailRegister(String toEmail, String token);
    void sendMailRetrievePassword(String toEmail, String token);
}
