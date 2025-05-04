package com.portfolio.interview.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data

public class UsersRoleId implements Serializable {
    private Long userSeq;
    private Long roleSeq;

    public UsersRoleId() {
    }

    public UsersRoleId(Long userSeq, Long roleSeq) {
        this.userSeq = userSeq;
        this.roleSeq = roleSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UsersRoleId that = (UsersRoleId) o;
        return Objects.equals(userSeq, that.userSeq) && Objects.equals(roleSeq, that.roleSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userSeq, roleSeq);
    }

}