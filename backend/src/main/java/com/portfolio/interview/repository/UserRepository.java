package com.portfolio.interview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByName(String name);

    Optional<User> findByEmailAndName(String name, String email);

    boolean existsById(String id);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
