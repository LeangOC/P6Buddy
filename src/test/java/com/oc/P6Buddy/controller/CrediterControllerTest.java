package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.CrediterService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CrediterControllerTest {

    private CrediterService crediterService;
    private CrediterController crediterController;
    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        crediterService = mock(CrediterService.class);
        crediterController = new CrediterController(crediterService);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    // ---------- TEST GET /crediter ----------
    //Affichage de la page /crediter avec un utilisateur
    @Test
    void testHomeWithLoggedUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(user);
        when(crediterService.getUserBalance(1)).thenReturn(200.0);

        // Act
        String viewName = crediterController.home(model, session);

        // Assert
        assertEquals("crediter", viewName);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("balance", 200.0);
    }

    //Redirection vers /login si l'utilisateur n'est pas connecté
    @Test
    void testHomeWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = crediterController.home(model, session);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(model);
    }

    // ---------- TEST POST /crediter ----------
    //Crédit d’un montant valide avec utilisateur connecté
    @Test
    void testCrediterWithLoggedUserAndValidAmount() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(user);
        when(crediterService.getUserBalance(1)).thenReturn(300.0);

        // Act
        String viewName = crediterController.crediter(100.0, session, model);

        // Assert
        assertEquals("crediter", viewName);
        verify(crediterService).crediterCompte(1, 100.0);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("balance", 300.0);
    }

    //Redirection vers /login si POST sans utilisateur
    @Test
    void testCrediterWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = crediterController.crediter(100.0, session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(crediterService);
        verifyNoInteractions(model);
    }

    //Gestion d'une erreur levée par le service (ex : montant négatif)
    @Test
    void testCrediterThrowsException() {
        // Arrange
        User user = new User();
        user.setId(1);

        when(session.getAttribute("loggedUser")).thenReturn(user);
        doThrow(new IllegalArgumentException("Montant invalide")).when(crediterService)
                .crediterCompte(1, -50.0);
        when(crediterService.getUserBalance(1)).thenReturn(150.0);

        // Act
        String viewName = crediterController.crediter(-50.0, session, model);

        // Assert
        assertEquals("crediter", viewName);
        verify(model).addAttribute("error", "Montant invalide");
        verify(model).addAttribute("balance", 150.0);
        verify(model).addAttribute("user", user);
    }
}
