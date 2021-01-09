package com.udacity.jwdnd.course4.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LoginUserRequest {

    @NotBlank
    @JsonProperty
    private String username;

    @NotNull
    @JsonProperty
    private String password;

    public LoginUserRequest(@NotBlank String username, @NotNull String password) {
        this.username = username;
        this.password = password;
    }

    public LoginUserRequest() {
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
}
