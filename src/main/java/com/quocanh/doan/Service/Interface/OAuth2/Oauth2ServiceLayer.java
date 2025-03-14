package com.quocanh.doan.Service.Interface.OAuth2;

import com.quocanh.doan.Model.User;
import com.quocanh.doan.config.Oauth2.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public interface Oauth2ServiceLayer {
    User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo);
    User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo);
    User storageUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest);
}
