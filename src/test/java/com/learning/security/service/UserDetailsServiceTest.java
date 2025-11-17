package com.learning.security.service;

import com.learning.security.entity.User;
import com.learning.security.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

class UserDetailsServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserDetailsService userDetailsService = new UserDetailsService(userRepository);

    @BeforeEach
    public void before() {
        User mockUser = User.builder()
                .email("user@email.com")
                .password("password")
                .build();

        BDDMockito
                .given(userRepository.findByEmail("user@email.com"))
                .willReturn(Optional.of(mockUser));
    }

    @Test
    @DisplayName("loadUserByUsername(): returns existing user")
    void loadUserByUsername() {
        var email = "user@email.com";
        var result = this.userRepository.findByEmail(email);
        Assertions.assertThat(result.isPresent()).isEqualTo(true);
        Assertions.assertThat(result.get().getEmail()).isEqualTo(email);
        

    }
}