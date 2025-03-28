package com.quocanh.doan.config.Oauth2.Service;

import com.quocanh.doan.config.Oauth2.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2ServiceUserInfo extends OAuth2UserInfo {
    public GoogleOAuth2ServiceUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

}
