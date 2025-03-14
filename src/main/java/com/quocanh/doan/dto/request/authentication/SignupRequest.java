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
    @NotNull(message = "First name must be provided.")
    private String firstName;

    @NotNull(message = "Last name must be provided.")
    private String lastName;

    private String email;
    private String password;
}
