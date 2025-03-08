package com.quocanh.doan.Exception;


import com.quocanh.doan.Exception.Applicant.ApplicantException;
import com.quocanh.doan.Exception.CheckCode.CheckCodeException;
import com.quocanh.doan.Exception.Company.CompanyExeptionHanlde;
import com.quocanh.doan.Exception.Job.JobExcetionHandler;
import com.quocanh.doan.Exception.Signin.InvalidCredenticalException;
import com.quocanh.doan.Exception.Signup.*;
import com.quocanh.doan.dto.request.ApiResponseProblemDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalException {
    private static final String MIN_ATTRIBUTE = "min";
    private ResponseEntity<ApiResponseProblemDetails> buildResponseEntity(String title, String instance,HttpStatus status,Exception exception, List<ErrorDetail> errors) {
        ApiResponseProblemDetails apiResponseProblemDetails = new ApiResponseProblemDetails();
        apiResponseProblemDetails.setType("https://datatracker.ietf.org/doc/html/rfc7807#ref-W3C.REC-html5-20141028");
        apiResponseProblemDetails.setTitle(title);
        apiResponseProblemDetails.setInstance(URI.create(instance));
        apiResponseProblemDetails.setStatus(status);

        if (errors != null && !errors.isEmpty()) {
            apiResponseProblemDetails.setProperties(Map.of("errors", errors));
        } else {
            apiResponseProblemDetails.setDetail(exception.getMessage());
        }

        return ResponseEntity.status(status).body(apiResponseProblemDetails);
    }
    @ExceptionHandler(value = {ExceptionClass.class})
    public ResponseEntity<ApiResponseProblemDetails> handleExceptionClass(ExceptionClass exception) {

        return buildResponseEntity(
                exception.getTitle(),
                exception.getInstance(),
                exception.getStatus(),
                exception,
                exception.getErrors()
        );
    }

//    @ExceptionHandler(value = { EmailExeption.class, PasswordException.class, EmailExistException.class, PasswordLengthException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleCustomExceptions(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        if (exception instanceof UsernameNotFoundException) {
//            title = "User name not found";
//            instance = "/authenticate/username/notfound";
//        } else if (exception instanceof EmailExeption) {
//            title = "Email validation";
//            instance = "/authenticate/email";
//        } else if (exception instanceof PasswordException) {
//            title = "Password exception";
//            instance = "/authenticate/password";
//        } else if (exception instanceof EmailExistException) {
//            title = "Email validation";
//            instance = "/authenticate/email";
//        } else if (exception instanceof PasswordLengthException) {
//            title = "Password exception";
//            instance = "/authenticate/password";
//        }
//        else {
//            title = "System exception";
//            instance = "/system/exception";
//        }
//        return buildResponseEntity(title, instance,status,exception);
//    }
//
//    @ExceptionHandler(value = {CheckCodeException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleCheckcode(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "System exception";
//        instance = "/system/checkcode";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//    @ExceptionHandler(value = {UserNotFoundException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleUserNotFound(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "User not found";
//        instance = "/system/auth";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//    @ExceptionHandler(value = {InvalidCredenticalException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleInvalidCredentical(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "Invalid credentical";
//        instance = "/system/auth";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//    @ExceptionHandler(value = {SignupException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleSignup(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "SignUp exception";
//        instance = "/system/auth";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//
//    @ExceptionHandler(value = {CompanyExeptionHanlde.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleExeptionCompany(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "Company exception";
//        instance = "/system/company";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//    @ExceptionHandler(value = {JobExcetionHandler.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleExeptionJob(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "Job exception";
//        instance = "/system/post";
//        return buildResponseEntity(title, instance,status,exception);
//    }
//
//    @ExceptionHandler(value = {ApplicantException.class})
//    public ResponseEntity<ApiResponseProblemDetails> handleExeptionApplicant(RuntimeException exception) {
//        String title;
//        String instance;
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        title = "Applicant exception";
//        instance = "/system/post";
//        return buildResponseEntity(title, instance,status,exception);
//    }
}
