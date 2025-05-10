package com.portfolio.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.UsersRolesId;
import com.portfolio.interview.entity.UsersRoles;

public interface UsersRolesRepository extends JpaRepository<UsersRoles, UsersRolesId> {
}