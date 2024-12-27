package com.quocanh.doan.dto.response;


import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String name;
    String email;
    Collection<? extends GrantedAuthority> authorities;
    boolean isNewUser;
    private String phone;
    private String university;
    String experiencelevel;
}
