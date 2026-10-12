package com.portfolio.userservice.service;

import com.portfolio.userservice.dto.UserRequest;
import com.portfolio.userservice.dto.UserResponse;
import com.portfolio.userservice.entity.User;
import com.portfolio.userservice.exception.EmailAlreadyExistsException;
import com.portfolio.userservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldCreateUser() {

        UserRequest request =
                new UserRequest("Daniel", "daniel2@example.com");

        User savedUser = new User(
                1L,
                "Daniel",
                "daniel2@example.com"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = userService.createUser(request);

        assertEquals(1L, response.id());
        assertEquals("Daniel", response.name());
        assertEquals("daniel2@example.com", response.email());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequest request =
                new UserRequest("Daniel", "daniel@example.com");

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUser(request)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
        void shouldGetAllUsers() {

        User user1 = new User(
                1L,
                "Daniel",
                "daniel@example.com"
        );

        User user2 = new User(
                2L,
                "Laura",
                "laura@example.com"
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserResponse> response =
                userService.getAllUsers();

        assertEquals(2, response.size());

        assertEquals("Daniel", response.get(0).name());
        assertEquals("daniel@example.com", response.get(0).email());

        assertEquals("Laura", response.get(1).name());
        assertEquals("laura@example.com", response.get(1).email());
        }
}