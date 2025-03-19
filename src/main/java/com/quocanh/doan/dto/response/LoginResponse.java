package com.quocanh.doan.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    Long refreshToken;
    boolean isNewUser;
    Collection<? extends GrantedAuthority> authorities;
}
