package com.portfolio.interview.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

/**
 * Primary key class for UsersRoles entity.
 */
@Embeddable
public class UsersRolesId implements Serializable {
    private Long user;
    private Long roles;

    public UsersRolesId() {
    }

    public UsersRolesId(Long user, Long roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UsersRolesId that = (UsersRolesId) o;
        return Objects.equals(user, that.user) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, roles);
    }
}