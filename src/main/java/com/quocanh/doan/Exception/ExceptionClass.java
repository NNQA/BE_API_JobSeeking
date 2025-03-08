package com.quocanh.doan.Exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionClass extends RuntimeException {
    private HttpStatus status;
    private String title;
    private List<ErrorDetail> errors;
    private String instance;
    public ExceptionClass(String message) {
        super(message);
    }
    public ExceptionClass(String title, HttpStatus status, List<ErrorDetail> errors, String instance) {
        super(errors.stream().map(ErrorDetail::getMessage).collect(Collectors.joining(", ")));
        this.title = title;
        this.status = status;
        this.errors = errors;
        this.instance = instance;
    }
}
