package com.lambda.watermyplants.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roleid;

    @NotNull
    @Column(nullable = false, unique = true)
    private String rolename;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "role", allowSetters = true)
    private List<UserRole> users = new ArrayList<>();

    public Role() {
    }

    public Role(@NotNull String rolename) {
        this.rolename = rolename.toUpperCase();
    }

    public long getRoleid() {
        return roleid;
    }

    public void setRoleid(long roleid) {
        this.roleid = roleid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public List<UserRole> getUsers() {
        return users;
    }

    public void setUsers(List<UserRole> users) {
        this.users = users;
    }
}
