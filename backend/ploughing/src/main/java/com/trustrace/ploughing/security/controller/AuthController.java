package com.trustrace.ploughing.security.controller;

import com.trustrace.ploughing.security.dto.AuthResponseDto;
import com.trustrace.ploughing.security.jwt.JwtGenerator;
import com.trustrace.ploughing.security.model.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${ploughing.build.version}/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserCredentials userCredentials){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getEmail(), userCredentials.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}