package com.portfolio.interview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UsersRoleId.class)
@Table(name = "users_roles")
public class UsersRoles {
    @Id
    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Id
    @Column(name = "role_seq", nullable = false)
    private Long roleSeq;

    // Getter와 Setter
    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public Long getRoleSeq() {
        return roleSeq;
    }

    public void setRoleSeq(Long roleSeq) {
        this.roleSeq = roleSeq;
    }
}
