package com.quocanh.doan.Service.Interface.UserIntef;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.request.authentication.LoginRequest;
import com.quocanh.doan.dto.request.authentication.ResetPasswordRequest;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import com.quocanh.doan.dto.response.LoginResponse;
import com.quocanh.doan.dto.response.User.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IUserService {
    User save(User user);
    User updateRoleUser(Long id);
    void updateNewUser(UserPrincipal userPrincipal,UserUpdate userUpdate);
    void deleteAll();
    List<User> getAllUsers();
    UserProfileResponse getProfileUser(Long id);
    void createUserProfile(Long id);
}
