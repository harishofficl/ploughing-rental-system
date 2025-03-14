package com.trustrace.ploughing.security.jwt;

import com.trustrace.ploughing.security.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import io.jsonwebtoken.*;

@Configuration
public class JwtGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwtGenerator.class);
    private final Key key = getSignInKey();

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(currDate.getTime() + SecurityConstants.getJwtExpiration());


        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currDate)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJWT(String token) {
        return Jwts
                .parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            throw new CredentialsExpiredException("JWT token has expired - " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw new AuthenticationServiceException("Unsupported JWT token - " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new AuthenticationServiceException("Malformed JWT token - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new BadCredentialsException("JWT token is invalid or empty - " + e.getMessage());
        }
    }

}