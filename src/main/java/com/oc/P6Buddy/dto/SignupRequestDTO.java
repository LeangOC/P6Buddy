package com.oc.P6Buddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequestDTO {

    @NotBlank(message = " ⚠\uFE0F Le nom d\u0027' utilisateur est obligatoire.")
    private String username;

    @Email(message = "Email invalide.")
    @NotBlank(message = " ⚠\uFE0F L\u0027' email est obligatoire.")
    private String email;

    @NotBlank(message = "⚠\uFE0F Le mot de passe est obligatoire.")
    private String password;

    // Getters et Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
