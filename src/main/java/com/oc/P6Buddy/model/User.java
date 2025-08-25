package com.oc.P6Buddy.model;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int IdUser;

    @Column(name = "username")
    private String UserName;

    @Column(name = "email" )
    private String Email;

    @Column(name = "password")
    private String Password;

    public int getIdUser() {
        return IdUser;
    }

    public String getUserName() {
        return UserName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
