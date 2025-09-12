package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Buddy;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.BuddyRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelationServiceTest {

    private UserRepository userRepository;
    private BuddyRepository buddyRepository;
    private RelationService relationService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        buddyRepository = mock(BuddyRepository.class);
        relationService = new RelationService(userRepository, buddyRepository);
    }

    // ------------- addRelation() ------------------
    //Ajout une relation normale puis Sauvegarde effectuée, message de succès
    @Test
    void testAddRelation_Success() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("me@example.com");

        User buddyUser = new User();
        buddyUser.setId(2);
        buddyUser.setEmail("buddy@example.com");

        when(userRepository.findByEmail("buddy@example.com")).thenReturn(Optional.of(buddyUser));
        when(buddyRepository.findByUserAndBuddy(currentUser, buddyUser)).thenReturn(Optional.empty());

        // Act
        String result = relationService.addRelation(currentUser, "buddy@example.com");

        // Assert
        assertEquals("Relation ajoutée avec succès ✅", result);
        verify(buddyRepository).save(any(Buddy.class));
    }

    // Email inconnu et Message d’erreur
    @Test
    void testAddRelation_UserNotFound() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act
        String result = relationService.addRelation(currentUser, "unknown@example.com");

        // Assert
        assertEquals("Aucun utilisateur trouvé avec cet email.", result);
        verify(buddyRepository, never()).save(any());
    }

    //Ajout de soi-même et Message d’erreur
    @Test
    void testAddRelation_SelfRelation() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("self@example.com");

        when(userRepository.findByEmail("self@example.com")).thenReturn(Optional.of(currentUser));

        // Act
        String result = relationService.addRelation(currentUser, "self@example.com");

        // Assert
        assertEquals("Vous ne pouvez pas vous ajouter vous-même.", result);
        verify(buddyRepository, never()).save(any());
    }

    //Relation déjà existante et Message d’erreur, pas de sauvegarde
    @Test
    void testAddRelation_AlreadyExists() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);

        User buddyUser = new User();
        buddyUser.setId(2);

        when(userRepository.findByEmail("buddy@example.com")).thenReturn(Optional.of(buddyUser));
        when(buddyRepository.findByUserAndBuddy(currentUser, buddyUser)).thenReturn(Optional.of(new Buddy()));

        // Act
        String result = relationService.addRelation(currentUser, "buddy@example.com");

        // Assert
        assertEquals("Cette relation existe déjà.", result);
        verify(buddyRepository, never()).save(any());
    }

    // ------------- getBuddyEmails() ------------------
    //Liste des buddies retournée et attendu : Liste correcte des emails
    @Test
    void testGetBuddyEmails_ReturnsList() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);

        User buddy1 = new User();
        buddy1.setEmail("a@buddy.com");

        User buddy2 = new User();
        buddy2.setEmail("b@buddy.com");

        Buddy b1 = new Buddy();
        b1.setUser(currentUser);
        b1.setBuddy(buddy1);

        Buddy b2 = new Buddy();
        b2.setUser(currentUser);
        b2.setBuddy(buddy2);

        when(buddyRepository.findByUser(currentUser)).thenReturn(List.of(b1, b2));

        // Act
        List<String> buddyEmails = relationService.getBuddyEmails(currentUser);

        // Assert
        assertEquals(2, buddyEmails.size());
        assertTrue(buddyEmails.contains("a@buddy.com"));
        assertTrue(buddyEmails.contains("b@buddy.com"));
    }
}
