package com.Ecomm.Ecomm.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private String name;
    private String email;
    private String otp;
    private Integer otpCount;
    private Long otpTimestamp;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_mapping", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @JsonIgnore
    public Long jwtTimestamp;

    private String forgotOtp;

    private Integer forgotOtpCount;

    private Long forgotOtpTimestamp;

    public User() {
    }

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getJwtTimestamp() {
        return jwtTimestamp;
    }

    public void setJwtTimestamp(Long jwtTimestamp) {
        this.jwtTimestamp = jwtTimestamp;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Integer getOtpCount() {
        return otpCount;
    }

    public void setOtpCount(Integer otpCount) {
        this.otpCount = otpCount;
    }

    public Long getOtpTimestamp() {
        return otpTimestamp;
    }

    public void setOtpTimestamp(Long otpTimestamp) {
        this.otpTimestamp = otpTimestamp;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getForgotOtp() {
        return forgotOtp;
    }

    public void setForgotOtp(String forgotOtp) {
        this.forgotOtp = forgotOtp;
    }

    public Integer getForgotOtpCount() {
        return forgotOtpCount;
    }

    public void setForgotOtpCount(Integer forgotOtpCount) {
        this.forgotOtpCount = forgotOtpCount;
    }

    public Long getForgotOtpTimestamp() {
        return forgotOtpTimestamp;
    }

    public void setForgotOtpTimestamp(Long forgotOtpTimestamp) {
        this.forgotOtpTimestamp = forgotOtpTimestamp;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
