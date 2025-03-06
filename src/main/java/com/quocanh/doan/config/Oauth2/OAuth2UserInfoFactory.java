package com.quocanh.doan.Security.Oauth2;

import com.quocanh.doan.Exception.OAuth2AuthenticationProcessingException;
import com.quocanh.doan.Model.AuthProvider;
import com.quocanh.doan.Security.Oauth2.Service.GoogleOAuth2ServiceUserInfo;
import org.springframework.security.core.parameters.P;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2ServiceUserInfo(attributes);
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
