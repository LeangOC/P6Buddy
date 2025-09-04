package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AuthService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }


    /**
     * Vérifie si l’email et le mot de passe correspondent.
     * (Prototype: mot de passe en clair pour simplifier le démarrage)
     */
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(rawPassword));
    }


    public User register(String username, String email, String rawPassword) {
        // Vérifie que l’email n’est pas déjà utilisé
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        });

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(rawPassword); //  en clair pour l’instant

        User savedUser = userRepository.save(user);


        // Création du compte avec balance = 0.0
        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(0.0);
        accountRepository.save(account);
        return savedUser;
    }

    public Double getUserBalance(Integer userId) {
        return accountRepository.findByUserId(userId)
                .map(Account::getBalance)
                .orElse(0.0);
    }


}

