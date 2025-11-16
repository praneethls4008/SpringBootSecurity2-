package com.learning.security.config;

import com.learning.security.entity.Token;
import com.learning.security.entity.User;
import com.learning.security.repository.TokenRepository;
import com.learning.security.service.JwtAccessTokenService;
import com.learning.security.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtAccessTokenService jwtAccessTokenService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        System.out.println("Inside JWT_FILTER_CHAIN");

        //if authHeader is null or authHeader does start with Bearer skip filter and continue to next filter
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            //recursive chain of filters so we return
            return;
        }

        //Bearer jwt_actual_token
        jwtToken = authHeader.substring(7);
        userEmail = jwtAccessTokenService.extractUsername(jwtToken);

        //if email not null and user is not logged in(no authentication)
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtAccessTokenService.isTokenValid(jwtToken, userDetails) && isTokenValidInRepo(jwtToken)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //we don't keep credentials in token so null
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                //set authentication in context now user is logged in
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            System.out.println("[DEBUG] Debugging Auth in JwtAuthenticationFilter.class");
            System.out.println("[DEBUG] Authentication is NULL");
        } else {
            System.out.println("[DEBUG] Debugging Auth in JwtAuthenticationFilter.class");
            System.out.println("[DEBUG] Authenticated user = " + auth.getName());
            System.out.println("[DEBUG] Authorities = " + auth.getAuthorities());
            System.out.println("[DEBUG] Authorities = " + auth.getAuthorities());
            System.out.println("[DEBUG] Auth class = " + auth.getClass().getSimpleName());
        }

        filterChain.doFilter(request, response);

    }

    private boolean isTokenValidInRepo(String token){
        Optional<Token> tokenInRepo = tokenRepository.findByToken(token);
        return tokenInRepo.isPresent() && !tokenInRepo.get().isRevoked() && !tokenInRepo.get().isExpired();
    }

}
