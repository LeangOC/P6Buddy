package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class ProfileService {
    private final UserRepository userRepository;
    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
            user.setPassword(password); // mot de passe mis à jour sans hash
        }

        return userRepository.save(user);
    }
}
