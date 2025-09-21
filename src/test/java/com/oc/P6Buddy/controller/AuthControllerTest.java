package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthService authService;
    private AuthController authController;
    private Model model;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
        model = mock(Model.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testLoginPageReturnsLoginView() {
        // Simple test pour vérifier la vue de la page de login
        String viewName = authController.loginPage();
        assertEquals("login", viewName);
    }
     //L'utilisateur est authentifié avec succès.
    @Test
    void testDoLoginSuccess() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail(email);

        when(authService.authenticate(email, password)).thenReturn(Optional.of(mockUser));
        when(authService.getUserBalance(1)).thenReturn(100.0);

        // Act
        String viewName = authController.doLogin(email, password, model, session);

        // Assert
        assertEquals("home", viewName);

        verify(session).setAttribute("loggedUser", mockUser);
        verify(model).addAttribute("user", mockUser);
        verify(model).addAttribute("balance", 100.0);
    }

    //L'utilisateur n'est pas authentifié (mauvais identifiants).
    @Test
    void testDoLoginFailure() {
        // Arrange
        String email = "wrong@example.com";
        String password = "wrongpassword";

        when(authService.authenticate(email, password)).thenReturn(Optional.empty());

        // Act
        String viewName = authController.doLogin(email, password, model, session);

        // Assert
        assertEquals("login", viewName); // Hypothèse : on reste sur login en cas d’échec (à adapter si autre comportement)
        // On vérifie que les bons messages d'erreur sont bien ajoutés au modèle
        verify(model).addAttribute("error", "⚠\uFE0F Email ou mot de passe incorrect. Veuillez réessayer");
        verify(model).addAttribute("enteredEmail", email);

        // On vérifie qu'aucune session n'est ouverte
        verify(session, never()).setAttribute(anyString(), any());
    }
}
