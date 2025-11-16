package com.learning.security.config;

import com.learning.security.enums.Permission;
import com.learning.security.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtLogoutHandler jwtLogoutHandler;

    @Bean
    @Order(1)
    SecurityFilterChain authChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/v1/auth/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .permitAll()
                );

        httpSecurity.logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler(jwtLogoutSuccessHandler)
                );

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain adminChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/v1/admin/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //role admin
                        .requestMatchers("/api/v1/admin/**")
                        .hasRole(Role.ADMIN.name())
                        //get req
                        .requestMatchers(HttpMethod.GET,"/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_READ.getPermission())
                        //post req
                        .requestMatchers(HttpMethod.POST,"/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_CREATE.getPermission())
                        //put req
                        .requestMatchers(HttpMethod.PUT,"/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_UPDATE.getPermission())
                        //delete req
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_DELETE.getPermission())

                        .anyRequest()
                        .denyAll()
                );

        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }



    @Bean
    @Order(3)
    SecurityFilterChain managerChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/v1/manager/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //role admin
                        .requestMatchers("/api/v1/manager/**")
                        .hasAnyRole(
                                Role.ADMIN.name(),
                                Role.MANAGER.name()
                        )
                        //get req
                        .requestMatchers(HttpMethod.GET,"/api/v1/manager/**")
                        .hasAnyAuthority(
                                Permission.ADMIN_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission()
                        )
                        //post req
                        .requestMatchers(HttpMethod.POST,"/api/v1/manager/**")
                        .hasAnyAuthority(
                                Permission.ADMIN_CREATE.getPermission(),
                                Permission.MANAGER_CREATE.getPermission()
                        )
                        //put req
                        .requestMatchers(HttpMethod.PUT,"/api/v1/manager/**")
                        .hasAnyAuthority(
                                Permission.ADMIN_UPDATE.getPermission(),
                                Permission.MANAGER_UPDATE.getPermission()
                        )
                        //delete req
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/manager/**")
                        .hasAnyAuthority(
                                Permission.ADMIN_DELETE.getPermission(),
                                Permission.MANAGER_DELETE.getPermission()
                        )

                        .anyRequest()
                        .denyAll()
                );

        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }


    @Bean
    @Order(4)
    SecurityFilterChain userChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher("/api/v1/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                );

        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

}
