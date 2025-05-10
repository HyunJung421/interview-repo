package com.portfolio.interview.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);

    boolean existsByName(String name);

}
