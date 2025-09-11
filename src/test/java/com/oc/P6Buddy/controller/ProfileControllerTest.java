package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    private ProfileService profileService;
    private ProfileController profileController;
    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        profileController = new ProfileController(profileService);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    // ---------- TEST GET /profile ----------

    @Test
    void testProfilePageWithLoggedInUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(user);

        // Act
        String viewName = profileController.profilePage(session, model);

        // Assert
        assertEquals("profile", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProfilePageWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = profileController.profilePage(session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verify(model, never()).addAttribute(anyString(), any());
    }

    // ---------- TEST POST /profile/update ----------

    @Test
    void testUpdateProfileSuccess() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1);
        loggedUser.setEmail("old@example.com");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setEmail("new@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        when(profileService.updateUser(1, "newName", "new@example.com", "newpass"))
                .thenReturn(updatedUser);

        // Act
        String viewName = profileController.updateProfile(
                "newName", "new@example.com", "newpass", session, model
        );

        // Assert
        assertEquals("profile", viewName);
        verify(session).setAttribute("loggedUser", updatedUser);
        verify(model).addAttribute("user", updatedUser);
        verify(model).addAttribute("success", "Profil mis à jour avec succès !");
    }

    @Test
    void testUpdateProfileWithNoUserInSession() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = profileController.updateProfile(
                "any", "any@example.com", "pass", session, model
        );

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(profileService);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void testUpdateProfileThrowsException() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1);
        loggedUser.setEmail("old@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        when(profileService.updateUser(1, "bad", "bad@example.com", "badpass"))
                .thenThrow(new IllegalArgumentException("Erreur de mise à jour"));

        // Act
        String viewName = profileController.updateProfile(
                "bad", "bad@example.com", "badpass", session, model
        );

        // Assert
        assertEquals("profile", viewName);
        verify(model).addAttribute("user", loggedUser);
        verify(model).addAttribute("error", "Erreur de mise à jour");
    }
}

