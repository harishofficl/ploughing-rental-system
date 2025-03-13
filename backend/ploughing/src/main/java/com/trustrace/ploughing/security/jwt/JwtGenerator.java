package com.trustrace.ploughing.security.jwt;

import com.trustrace.ploughing.security.SecurityConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;


@Configuration
public class JwtGenerator {
    private final Key key = getSignInKey();
    public String generateToken(Authentication authentication){
        String email = authentication.getName();
        Date currDate = new Date(System.currentTimeMillis());
        Date expiryDate =  new Date(currDate.getTime() + SecurityConstants.getJwtExpiration());


        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currDate)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJWT(String token){
        return Jwts
                .parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Malformed JWT token");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid");
        }
    }
}