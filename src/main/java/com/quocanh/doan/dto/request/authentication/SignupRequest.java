package com.quocanh.doan.dto.request.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotNull(message = "Email must be provided")
    private String email;
    @NotNull(message = "Password must be provided")
    private String password;
}
