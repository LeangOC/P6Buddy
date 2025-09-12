package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrediterServiceTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private CrediterService crediterService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        crediterService = new CrediterService(userRepository, accountRepository);
    }

    // ----------- getUserBalance() -----------
    //Compte trouvé et Retourne le solde
    @Test
    void testGetUserBalance_Found() {
        // Arrange

        int userId = 1;
        Account account = new Account();
        account.setBalance(100.0);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

        // Act
        double balance = crediterService.getUserBalance(userId);

        // Assert
        assertEquals(100.0, balance);
    }

    // Compte non trouvé et Retourne 0.0
    @Test
    void testGetUserBalance_NotFound() {
        // Arrange
        int userId = 2;
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        double balance = crediterService.getUserBalance(userId);

        // Assert
        assertEquals(0.0, balance);
    }

    // ----------- crediterCompte() -----------
    //Montant positif, compte existant et Solde mis à jour, sauvegarde faite
    @Test
    void testCrediterCompte_Success() {
        // Arrange
        int userId = 1;
        double montant = 50.0;

        Account account = new Account();
        account.setBalance(100.0);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

        // Act
        crediterService.crediterCompte(userId, montant);

        // Assert
        assertEquals(150.0, account.getBalance());
        verify(accountRepository).save(account);
    }

    // Montant positif, compte existant et Solde mis à jour, sauvegarde faite
    @Test
    void testCrediterCompte_InvalidMontant_Negatif() {
        // Arrange
        int userId = 1;
        double montant = -10.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            crediterService.crediterCompte(userId, montant);
        });

        assertEquals("Le montant doit être positif", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    // Montant null et IllegalArgumentException
    @Test
    void testCrediterCompte_InvalidMontant_Null() {
        // Arrange
        int userId = 1;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            crediterService.crediterCompte(userId, null);
        });

        assertEquals("Le montant doit être positif", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    //Compte introuvable et RuntimeException
    @Test
    void testCrediterCompte_AccountNotFound() {
        // Arrange
        int userId = 99;
        double montant = 30.0;

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            crediterService.crediterCompte(userId, montant);
        });

        assertTrue(exception.getMessage().contains("Compte introuvable pour l'utilisateur"));
        verify(accountRepository, never()).save(any());
    }
}
