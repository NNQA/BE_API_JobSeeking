package com.quocanh.doan.Service.Interface.UserIntef;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.dto.request.authentication.SignupRequest;
import org.springframework.security.core.Authentication;

public interface IUserService {
    User save(User user);
    void signUp(SignupRequest signupRequest);
    void VerifiedCode(String email, String code);
    User updateRoleUser(Long id);
//    void deleteAll();
//    List<User> getAllUsers();
}
