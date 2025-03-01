package com.TheOffice.theOffice.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class User {
    private Long id;
    @Email
    @NotBlank (message = "L'email ne peut pas être vide")
    private String email;
    @NotBlank (message = "L'identifiant ne peut pas être vide")
    private String username;
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;
    private String role;
    private BigDecimal wallet;

    // Constructor
    public User(Long id,String email, String username, String password, String role, BigDecimal wallet) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.wallet = wallet;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }
}
