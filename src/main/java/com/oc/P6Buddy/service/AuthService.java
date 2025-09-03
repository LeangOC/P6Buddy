package com.oc.P6Buddy.service;


import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
public class AuthService {
    private final UserRepository userRepository;


    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        return userRepository.save(user);
    }

}