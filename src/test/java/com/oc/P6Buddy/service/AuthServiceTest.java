package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        accountRepository = mock(AccountRepository.class);
        authService = new AuthService(userRepository, accountRepository);
    }

    // ---------- AUTHENTICATE ----------
    //Utilisateur trouvé et mot de passe correct
    @Test
    void testAuthenticateSuccess() {
        // Arrange : Mise en place du contexte
        //On définit les données d'entrée
        String email = "test@example.com";
        String password = "password";
        //-On crée un objet User simulé
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        //On mocke (simulate ) le comportement du UserRepository
        //Si findByEmail(email) est appelé avec "test@example.com",
        // alors il retournera un Optional contenant notre utilisateur fictif
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act : (exécution de la méthode à tester)
        Optional<User> result = authService.authenticate(email, password);
        //le result peut être présent (isPresent) ou absent (pour éviter NullPonterException)

        // Assert : Vérification du résultat
        assertTrue(result.isPresent()); //On vérifie que le résultat n’est pas vide
        assertEquals(email, result.get().getEmail());//On vérifie que l’objet User retourne le bon email
    }

    //Utilisateur trouvé et mot de passe correct
    @Test
    void testAuthenticateWrongPassword() {
        // Arrange
        String email = "test@example.com";
        String correctPassword = "password";
        String wrongPassword = "wrongpass";

        User user = new User();
        user.setEmail(email);
        user.setPassword(correctPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = authService.authenticate(email, wrongPassword);

        // Assert
        assertTrue(result.isEmpty());
    }

    //Utilisateur non trouvé
    @Test
    void testAuthenticateUserNotFound() {
        // Arrange
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = authService.authenticate(email, "password");

        // Assert
        assertTrue(result.isEmpty());
    }

    // ---------- GET USER BALANCE ----------
    //Solde trouvé pour l'utilisateur
    @Test
    void testGetUserBalanceFound() {
        // Arrange
        int userId = 1;
        Account account = new Account();
        account.setBalance(150.50);

        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

        // Act
        double balance = authService.getUserBalance(userId);

        // Assert
        assertEquals(150.50, balance);
    }

    //Aucun compte trouvé → retourne 0.0
    @Test
    void testGetUserBalanceNotFound() {
        // Arrange
        int userId = 2;
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        double balance = authService.getUserBalance(userId);

        // Assert
        assertEquals(0.0, balance);
    }
}
