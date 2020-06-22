package com.lambda.watermyplants.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    private String phonenumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "user", allowSetters = true)
    private List<UserRole> roles = new ArrayList<>();

    public User() {
    }

    public User(@NotNull String username, @NotNull String password, String phonenumber, List<UserRole> roles) {
        setUsername(username);
        setPassword(password);
        this.phonenumber = phonenumber;
        for (UserRole ur : roles) {
            ur.setUser(this);
        }
        this.roles = roles;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordNoEncrypt(String password) {
        this.password = password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(new UserRole(this, role));
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        for (UserRole r : this.roles) {
            String myRole = "ROLE_" + r.getRole().getRolename().toUpperCase();
            authorityList.add(new SimpleGrantedAuthority(myRole));
        }

        return authorityList;
    }
}
