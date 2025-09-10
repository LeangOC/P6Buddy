package com.oc.P6Buddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequestDTO {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire.")
    private String username;

    @Email(message = "Email invalide.")
    @NotBlank(message = "L'email est obligatoire.")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire.")
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
