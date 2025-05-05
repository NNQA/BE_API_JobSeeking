package com.quocanh.doan.dto.request.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    @NotNull(message = "Email must be provided")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotNull(message = "Password must be provided")
    @Size(min = 4,message = "Password needs to be at least 4 characters")
    private String password;
}
