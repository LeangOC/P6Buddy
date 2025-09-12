package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private UserRepository userRepository;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        profileService = new ProfileService(userRepository);
    }

    //Mise à jour du nom, email et password puis Mot de passe changé
    @Test
    void testUpdateUser_Success_WithPassword() {
        // Arrange
        int userId = 1;
        String newUsername = "newUser";
        String newEmail = "new@example.com";
        String newPassword = "newpass";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUser");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldpass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = profileService.updateUser(userId, newUsername, newEmail, newPassword);

        // Assert
        assertEquals(newUsername, updatedUser.getUsername());
        assertEquals(newEmail, updatedUser.getEmail());
        assertEquals(newPassword, updatedUser.getPassword());
        verify(userRepository).save(updatedUser);
    }

    // Mise à jour nom/email, password inchangé puis Mot de passe conservé
    @Test
    void testUpdateUser_Success_WithoutPasswordChange() {
        // Arrange
        int userId = 2;
        String newUsername = "newUser2";
        String newEmail = "new2@example.com";
        String unchangedPassword = "oldpass";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUser2");
        existingUser.setEmail("old2@example.com");
        existingUser.setPassword(unchangedPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = profileService.updateUser(userId, newUsername, newEmail, "");

        // Assert
        assertEquals(newUsername, updatedUser.getUsername());
        assertEquals(newEmail, updatedUser.getEmail());
        assertEquals(unchangedPassword, updatedUser.getPassword()); // password must stay the same
        verify(userRepository).save(updatedUser);
    }

    //Mise à jour nom/email, password inchangé et Mot de passe conservé
    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        int userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            profileService.updateUser(userId, "username", "email@example.com", "pass");
        });

        assertEquals("Utilisateur introuvable", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
