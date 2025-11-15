package com.learning.security.controller;

import com.learning.security.dto.request.AuthenticationRequest;
import com.learning.security.dto.request.RegisterRequest;
import com.learning.security.dto.response.AuthenticationTokensResponse;
import com.learning.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws IOException
    {
        AuthenticationTokensResponse authTokens = authenticationService.register(registerRequest);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", authTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("auth/refresh")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(authTokens.getAccessToken());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest authRequest
    ){

        AuthenticationTokensResponse authTokens = authenticationService.authenticate(authRequest);

        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", authTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("auth/refresh")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(authTokens.getAccessToken());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshAccessToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) throws IOException {


        System.out.println("inside refresh controller!");

        AuthenticationTokensResponse authTokens = authenticationService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok()
                .body(authTokens.getAccessToken());
    }

}
