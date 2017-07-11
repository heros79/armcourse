package com.course.aca.model.dto;

/**
 * Data Transfer Object for Authorization information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class LoginRequest {
    private String username;
    private String password;

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
}
