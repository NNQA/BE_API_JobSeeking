package com.quocanh.doan.dto.request.authentication;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotNull(message = "Email must be provided")
    @NotBlank(message = "Email cannot be blank")
    String email;
    @NotNull(message = "Password must be provided")
    @NotBlank(message = "Password cannot be blank")
    String password;
}
