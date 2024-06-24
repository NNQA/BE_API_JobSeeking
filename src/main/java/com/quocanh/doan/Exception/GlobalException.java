package com.quocanh.doan.Exception;


import com.quocanh.doan.Exception.CheckCode.CheckCodeException;
import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import com.quocanh.doan.Exception.Signup.*;
import com.quocanh.doan.dto.request.ApiResponseProblemDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class GlobalException {
    private static final String MIN_ATTRIBUTE = "min";
    private ResponseEntity<ApiResponseProblemDetails> buildResponseEntity(String title, String instance,HttpStatus status,Exception exception) {
        ApiResponseProblemDetails apiResponseProblemDetails = new ApiResponseProblemDetails();
        apiResponseProblemDetails.setType("https://datatracker.ietf.org/doc/html/rfc7807#ref-W3C.REC-html5-20141028");
        apiResponseProblemDetails.setTitle(title);
        apiResponseProblemDetails.setInstance(URI.create(instance));
        apiResponseProblemDetails.setDetail(exception.getMessage());
        apiResponseProblemDetails.setStatus(status);
        return ResponseEntity.badRequest().body(apiResponseProblemDetails);
    }


    @ExceptionHandler(value = { EmailExeption.class, PasswordException.class, EmailExistException.class, PasswordLengthException.class})
    public ResponseEntity<ApiResponseProblemDetails> handleCustomExceptions(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (exception instanceof UsernameNotFoundException) {
            title = "User name not found";
            instance = "/authenticate/username/notfound";
        } else if (exception instanceof EmailExeption) {
            title = "Email validation";
            instance = "/authenticate/email";
        } else if (exception instanceof PasswordException) {
            title = "Password exception";
            instance = "/authenticate/password";
        } else if (exception instanceof EmailExistException) {
            title = "Email validation";
            instance = "/authenticate/email";
        } else if (exception instanceof PasswordLengthException) {
            title = "Password exception";
            instance = "/authenticate/password";
        }
        else {
            title = "System exception";
            instance = "/system/exception";
        }
        return buildResponseEntity(title, instance,status,exception);
    }

    @ExceptionHandler(value = {CheckCodeException.class})
    public ResponseEntity<ApiResponseProblemDetails> handleCheckcode(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        title = "System exception";
        instance = "/system/checkcode";
        return buildResponseEntity(title, instance,status,exception);
    }
    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ApiResponseProblemDetails> handleUserNotFound(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        title = "User not found";
        instance = "/system/auth";
        return buildResponseEntity(title, instance,status,exception);
    }
    @ExceptionHandler(value = {InvalidCredenticalException.class})
    public ResponseEntity<ApiResponseProblemDetails> handleInvalidCredentical(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        title = "Invalid credentical";
        instance = "/system/auth";
        return buildResponseEntity(title, instance,status,exception);
    }
    @ExceptionHandler(value = {SignupException.class})
    public ResponseEntity<ApiResponseProblemDetails> handleSignup(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        title = "SignUp exception";
        instance = "/system/auth";
        return buildResponseEntity(title, instance,status,exception);
    }

    @ExceptionHandler(value = {CompanyExeptionHanlde.class})
    public ResponseEntity<ApiResponseProblemDetails> handleExeptionCompany(RuntimeException exception) {
        String title;
        String instance;
        HttpStatus status = HttpStatus.BAD_REQUEST;
        title = "Company exception";
        instance = "/system/company";
        return buildResponseEntity(title, instance,status,exception);
    }
}
