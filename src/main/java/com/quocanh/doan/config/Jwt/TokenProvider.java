package com.quocanh.doan.config.Jwt;


import com.quocanh.doan.config.AppProperties;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    private final String COOKIE_JWT = "cookie_authenticated";

    public ResponseCookie generateJwtCookie(Authentication authentication) {
        String jwt = generateToken(authentication);

        return ResponseCookie.from(COOKIE_JWT, jwt).path("/").maxAge(24 * 60 * 60).httpOnly(true).build();
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_JWT,null).path("/").build();
        return responseCookie;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build().parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(appProperties.getAuth().getTokenSecret().getBytes());
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());


        Claims claims = Jwts.claims().setSubject(userPrincipal.getId().toString());
        claims.put("roles", userPrincipal.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(),SignatureAlgorithm.HS512)
                .compact();
    }
}
