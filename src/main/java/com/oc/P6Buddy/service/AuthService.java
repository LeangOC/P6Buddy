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

    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(rawPassword));
    }

    public User register(String username, String email, String rawPassword) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        });

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(rawPassword);

        User savedUser = userRepository.save(user);

        // Créer automatiquement un compte avec solde = 0
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

    public User updateUser(Integer userId, String username, String email, String password) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur introuvable");
        }

        User user = optionalUser.get();
        user.setUsername(username);
        user.setEmail(email);

        if (password != null && !password.isBlank()) {
            user.setPassword(password); // mot de passe en clair
        }

        return userRepository.save(user);
    }


}
