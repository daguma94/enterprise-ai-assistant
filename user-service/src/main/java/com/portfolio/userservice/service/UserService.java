package com.portfolio.userservice.service;

import com.portfolio.userservice.dto.UserRequest;
import com.portfolio.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();
}