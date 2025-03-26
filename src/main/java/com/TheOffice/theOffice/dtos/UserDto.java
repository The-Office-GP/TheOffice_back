package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.User;

import java.math.BigDecimal;

public class UserDto {
    private long id;
    private String email;
    private String username;
    private String role;
    private BigDecimal wallet;

    public UserDto() {

    }

    // Constructor
    public UserDto(long id, String email, String username, String role, BigDecimal wallet) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.wallet = wallet;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public static UserDto fromEntity(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getRole(), user.getWallet());
    }
}
