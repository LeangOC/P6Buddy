package com.oc.P6Buddy.service;


import com.oc.P6Buddy.model.Users;
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
    public Optional<Users> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(rawPassword));
    }
}