package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SignupServiceTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private SignupService signupService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        signupService = new SignupService(userRepository, accountRepository);
    }

    @Test
    void register_shouldCreateUserAndAccount_whenEmailNotUsed() {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Simuler le user sauvegardé
        User savedUser = new User();
        savedUser.setId(1); // simuler un ID généré
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword(password);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = signupService.register(username, email, password);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword()); // attention : mot de passe en clair

        // Vérifier que le user est bien sauvegardé
        verify(userRepository).save(any(User.class));

        // Vérifier que le compte a bien été créé avec le bon user
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertNotNull(capturedAccount);
        assertEquals(savedUser, capturedAccount.getUser());
        assertEquals(0.0, capturedAccount.getBalance());
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyUsed() {
        // Arrange
        String email = "used@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            signupService.register("someone", email, "pass");
        });

        assertEquals("Cet email est déjà utilisé !", exception.getMessage());

        verify(userRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }
}
