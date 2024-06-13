package com.quocanh.doan.Security.Oauth2;

import com.quocanh.doan.Exception.BadRequetException;
import com.quocanh.doan.Security.AppProperties;
import com.quocanh.doan.Security.Jwt.TokenProvider;
import com.quocanh.doan.Service.ImplementService.User.UserRefreshTokenService;
import com.quocanh.doan.Utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    private final AppProperties appProperties;

    private final TokenProvider tokenProvider;
    private final UserRefreshTokenService userRefreshTokenService;
    public  OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                UserRefreshTokenService userRefreshTokenService,
                HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository) {
        this.httpCookieOauth2AuthorizationRequestRepository = httpCookieOauth2AuthorizationRequestRepository;
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.userRefreshTokenService = userRefreshTokenService;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUrl = CookieUtils.getCookie(request, HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUrl.isPresent() && !isAuthorizeRedirectUri(redirectUrl.get())) {
            throw new BadRequetException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUri = redirectUrl.orElse(getDefaultTargetUrl());
        String token = tokenProvider.generateToken(authentication);

        Long refreshToken = userRefreshTokenService.saveTokenRequest(authentication);
        return UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("accessToken", token)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizeRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream().
                anyMatch(
                        redirectUri -> {
                            URI authorizeURI = URI.create(redirectUri);
                            return authorizeURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                    && authorizeURI.getPort() == clientRedirectUri.getPort();
                        }
                );

    }
}
