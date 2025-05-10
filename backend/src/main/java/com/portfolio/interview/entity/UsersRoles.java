package com.portfolio.interview.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_roles")
public class UsersRoles {

    @EmbeddedId
    private UsersRolesId id;

    @MapsId("user")
    @ManyToOne
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @MapsId("roles")
    @ManyToOne
    @JoinColumn(name = "role_seq", nullable = false)
    private Roles roles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

}
