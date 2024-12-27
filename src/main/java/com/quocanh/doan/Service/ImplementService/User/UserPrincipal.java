package com.quocanh.doan.Service.ImplementService.User;

import com.quocanh.doan.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String email;
    private String password;
    private boolean isNewUser;
    private String name;
    private String phone;
    private String university;
    private String experiencelevel;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities,
                         boolean isNewUser, String university, String phone, String name, String experiencelevel ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isNewUser = isNewUser;
        this.university = university;
        this.name = name;
        this.phone = phone;
        this.experiencelevel = experiencelevel;
    }

    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorityList = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorityList,
                user.isNewUser(),
                user.getUniversity(),
                user.getPhone(),
                user.getName(),
                user.getExperiencelevel()
        );
    }

    public static UserPrincipal build(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.build(user);

        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }

    public boolean getIsNewUser() {
        return isNewUser;
    }
    public String getEmail() {
        return email;
    }
    public String getExperiencelevel() {
        return experiencelevel;
    }
    public String getPhone() {
        return phone;
    }
    public String getUniversity() {
        return university;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
