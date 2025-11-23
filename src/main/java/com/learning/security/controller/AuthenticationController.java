package com.learning.security.controller;

import com.learning.security.dto.request.AuthenticationRequest;
import com.learning.security.dto.request.RegisterRequest;
import com.learning.security.dto.response.AuthenticationTokensResponse;
import com.learning.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService authenticationService;


    // -------------------------------------------------------------------------
    // Register
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Register new user",
            description = """
                    Creates a new user account and issues both access and refresh tokens.
                    Refresh token is stored in a secure HttpOnly cookie.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Required registration details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User registered successfully. Returns access token."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data."
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws IOException {
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


    // -------------------------------------------------------------------------
    // Authenticate (login)
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Authenticate user (login)",
            description = """
                    Validates user credentials and returns a new access token.
                    Refresh token is re-issued and stored in a secure HttpOnly cookie.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authenticated successfully."),
                    @ApiResponse(responseCode = "401", description = "Invalid email or password.")
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest authRequest
    ) {

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


    // -------------------------------------------------------------------------
    // Refresh Token
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Refresh access token",
            description = """
                    Generates a new access token using the refresh token stored in HttpOnly cookie.
                    This endpoint never exposes the refresh token to client-side JavaScript.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "New access token returned."),
                    @ApiResponse(responseCode = "401", description = "Refresh token invalid or expired."),
            }
    )
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
