package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.RelationService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RelationControllerTest {

    private RelationService relationService;
    private RelationController relationController;
    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        relationService = mock(RelationService.class);
        relationController = new RelationController(relationService);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    // ---------- TEST GET /relation ----------
    //L’utilisateur est connecté, la liste des relations est affichée
    @Test
    void testRelationPageWithLoggedUser() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("user@example.com");

        List<String> mockBuddyList = List.of("buddy1@example.com", "buddy2@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(currentUser);
        when(relationService.getBuddyEmails(currentUser)).thenReturn(mockBuddyList);

        // Act
        String viewName = relationController.relationPage(session, model);

        // Assert
        assertEquals("relation", viewName);
        verify(model).addAttribute("relations", mockBuddyList);
    }

    //L’utilisateur n’est pas connecté → redirection vers /login
    @Test
    void testRelationPageWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = relationController.relationPage(session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(relationService);
        verify(model, never()).addAttribute(anyString(), any());
    }

    // ---------- TEST POST /relation ----------
    //Ajout d’un buddy avec un utilisateur connecté
    @Test
    void testAddRelationWithLoggedUser() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("user@example.com");

        String buddyEmail = "buddy@example.com";
        String successMessage = "Relation ajoutée avec succès.";
        List<String> updatedBuddyList = List.of("buddy@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(currentUser);
        when(relationService.addRelation(currentUser, buddyEmail)).thenReturn(successMessage);
        when(relationService.getBuddyEmails(currentUser)).thenReturn(updatedBuddyList);

        // Act
        String viewName = relationController.addRelation(buddyEmail, session, model);

        // Assert
        assertEquals("relation", viewName);
        verify(relationService).addRelation(currentUser, buddyEmail);
        verify(model).addAttribute("message", successMessage);
        verify(model).addAttribute("relations", updatedBuddyList);
    }

    //Tentative d’ajout de buddy sans utilisateur connecté
    @Test
    void testAddRelationWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = relationController.addRelation("buddy@example.com", session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(relationService);
        verify(model, never()).addAttribute(anyString(), any());
    }
}

