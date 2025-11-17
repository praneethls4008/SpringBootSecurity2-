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
                .email("user_present@email.com")
                .password("password")
                .build();

        //return Optional<User>
        BDDMockito
                .given(userRepository.findByEmail("user_present@email.com"))
                .willReturn(Optional.of(mockUser));

        //return Optional<Empty>
        BDDMockito
                .given(userRepository.findByEmail("user_not_present@email.com"))
                .willReturn(Optional.empty());
    }

    @Test
    @DisplayName("loadUserByUsername(): returns existing user")
    void loadUser_success() {
        var email = "user_present@email.com";
        var result = this.userRepository.findByEmail(email);
        Assertions.assertThat(result.isPresent()).isEqualTo(true);
        Assertions.assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("loadUserByUsername(): returns existing user")
    void loadUser_notFound() {
        var email = "user_not_present@email.com";
        var result = this.userRepository.findByEmail(email);
        Assertions.assertThat(result.isEmpty()).isEqualTo(true);
        Assertions.assertThat(result.isPresent()).isEqualTo(false);
    }

}