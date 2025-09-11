package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.Transaction;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.TransactionRepository;
import com.oc.P6Buddy.service.RelationService;
import com.oc.P6Buddy.service.TransferService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    private TransferService transferService;
    private TransactionRepository transactionRepository;
    private RelationService relationService;
    private TransferController transferController;
    private HttpSession session;
    private Model model;

    @BeforeEach
    void setUp() {
        transferService = mock(TransferService.class);
        transactionRepository = mock(TransactionRepository.class);
        relationService = mock(RelationService.class);
        transferController = new TransferController(transferService, transactionRepository, relationService);
        session = mock(HttpSession.class);
        model = mock(Model.class);
    }

    // ---------- TEST GET /transfer ----------

    @Test
    void testTransferPageWithLoggedUser() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1);

        List<Transaction> transactions = List.of(new Transaction());
        List<String> buddies = List.of("buddy@example.com");

        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        when(transactionRepository.findBySender(loggedUser)).thenReturn(transactions);
        when(relationService.getBuddyEmails(loggedUser)).thenReturn(buddies);

        // Act
        String viewName = transferController.transferPage(session, model);

        // Assert
        assertEquals("transfer", viewName);
        verify(model).addAttribute("transactions", transactions);
        verify(model).addAttribute("relations", buddies);
    }

    @Test
    void testTransferPageWithNoUser() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = transferController.transferPage(session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(relationService);
    }

    // ---------- TEST POST /transfer ----------

    @Test
    void testDoTransferSuccess() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1);

        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        List<Transaction> transactions = List.of(new Transaction());
        List<String> buddies = List.of("buddy@example.com");

        when(transactionRepository.findBySender(loggedUser)).thenReturn(transactions);
        when(relationService.getBuddyEmails(loggedUser)).thenReturn(buddies);

        // Act
        String viewName = transferController.doTransfer("buddy@example.com", "test desc", 100.0, session, model);

        // Assert
        assertEquals("transfer", viewName);
        verify(transferService).transfer(loggedUser, "buddy@example.com", "test desc", 100.0);
        verify(model).addAttribute(eq("success"), contains("Paiement effectué avec succès"));
        verify(model).addAttribute("transactions", transactions);
        verify(model).addAttribute("relations", buddies);
    }

    @Test
    void testDoTransferWithException() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1);

        when(session.getAttribute("loggedUser")).thenReturn(loggedUser);
        doThrow(new IllegalArgumentException("Erreur de transfert")).when(transferService)
                .transfer(any(), any(), any(), any());

        List<Transaction> transactions = List.of();
        List<String> buddies = List.of();

        when(transactionRepository.findBySender(loggedUser)).thenReturn(transactions);
        when(relationService.getBuddyEmails(loggedUser)).thenReturn(buddies);

        // Act
        String viewName = transferController.doTransfer("buddy@example.com", "fail", 0.0, session, model);

        // Assert
        assertEquals("transfer", viewName);
        verify(model).addAttribute("error", "Erreur de transfert");
        verify(model).addAttribute("transactions", transactions);
        verify(model).addAttribute("relations", buddies);
    }

    @Test
    void testDoTransferWithNoUser() {
        // Arrange
        when(session.getAttribute("loggedUser")).thenReturn(null);

        // Act
        String viewName = transferController.doTransfer("email", "desc", 10.0, session, model);

        // Assert
        assertEquals("redirect:/login", viewName);
        verifyNoInteractions(transferService);
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(relationService);
    }
}
