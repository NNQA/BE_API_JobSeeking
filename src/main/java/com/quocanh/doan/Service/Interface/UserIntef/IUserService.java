package com.quocanh.doan.Service.Interface.UserIntef;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.dto.request.authentication.SignupRequest;

public interface IUserService {
    User save(User user);
    void signUp(SignupRequest signupRequest);
//    void deleteAll();
//    List<User> getAllUsers();
}
