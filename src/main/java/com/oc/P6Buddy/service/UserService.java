package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.users;
import com.oc.P6Buddy.repository.UserRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    @Autowired
    private UserRepositroy userRepositroy;
    public Iterable<users> getUsers() {return userRepositroy.findAll();}
    }
