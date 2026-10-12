package com.portfolio.userservice.repository;

import com.portfolio.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}