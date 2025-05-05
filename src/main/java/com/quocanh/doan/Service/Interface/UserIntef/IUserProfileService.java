package com.quocanh.doan.Service.Interface.UserIntef;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.dto.response.User.UserProfileResponse;

public interface IUserProfileService {
    UserProfileResponse getProfileUser(Long UserId);
    void CreateUserProfileUser(Long id);
}
