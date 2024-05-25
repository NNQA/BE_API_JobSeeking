package com.quocanh.doan.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ProblemDetail;

import java.net.URI;

@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseProblemDetails extends ProblemDetail {
    public void setType(String url) {
        super.setType(URI.create(url));
    }
}
