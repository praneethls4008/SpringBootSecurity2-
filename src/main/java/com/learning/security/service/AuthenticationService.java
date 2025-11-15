package com.learning.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.security.dto.request.AuthenticationRequest;
import com.learning.security.dto.request.RegisterRequest;
import com.learning.security.dto.response.AuthenticationTokensResponse;
import com.learning.security.entity.Token;
import com.learning.security.entity.User;
import com.learning.security.enums.Role;
import com.learning.security.enums.TokenType;
import com.learning.security.repository.TokenRepository;
import com.learning.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAccessTokenService jwtAccessTokenService;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationTokensResponse register(RegisterRequest request) throws IOException {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var accessToken = jwtAccessTokenService.generateToken(user);
        var refreshToken = jwtRefreshTokenService.generateRefreshToken(user);
        saveUserToken(accessToken, savedUser);
        return AuthenticationTokensResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);

    }

    private void saveUserToken(String jwtToken, User savedUser) {
        var token = Token
                .builder()
                .token(jwtToken)
                .user(savedUser)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationTokensResponse authenticate(AuthenticationRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow();
        var accessToken = jwtAccessTokenService.generateToken(user);
        var refreshToken = jwtRefreshTokenService.generateRefreshToken(user);

        //revoke all old token when user logs in and generate new token
        revokeAllUserTokens(user);
        saveUserToken(accessToken, user);
        return AuthenticationTokensResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public AuthenticationTokensResponse refreshAccessToken(String refreshToken) throws IOException {
        final String userEmail;

        userEmail = jwtRefreshTokenService.extractUsername(refreshToken);

        if(userEmail!=null){
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if(jwtRefreshTokenService.isTokenValid(refreshToken, user)){
                var accessToken = jwtAccessTokenService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(accessToken, user);
                return AuthenticationTokensResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }

        return null;
    }
}
