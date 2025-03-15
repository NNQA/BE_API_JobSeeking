package com.quocanh.doan.Service.Interface.UserIntef;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import com.quocanh.doan.dto.request.UserUpdate;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IUserService {
    User save(User user);
    void signUp(SignupRequest signupRequest, BindingResult result);
    void verifyEmailWithToken(String token);
    User updateRoleUser(Long id);
    void updateNewUser(UserPrincipal userPrincipal,UserUpdate userUpdate);
    void deleteAll();
    List<User> getAllUsers();
    void sendRequestEmailAgain(String email);
}
