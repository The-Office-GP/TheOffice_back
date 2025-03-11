package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.User;

public class UserDto {
    private long id;
    private String email;
    private String username;
    private String role;

    public UserDto() {

    }

    public UserDto(long id, String email, String username, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

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

    public static UserDto fromEntity(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }
}
