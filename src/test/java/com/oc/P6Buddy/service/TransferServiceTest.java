package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.Transaction;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.TransactionRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        transferService = new TransferService(accountRepository, userRepository, transactionRepository);
    }

    @Test
    void transfer_shouldTransferAmountSuccessfully() {
        // Arrange
        User sender = new User();
        sender.setId(1);
        sender.setEmail("sender@example.com");

        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        Account senderAccount = new Account();
        senderAccount.setUser(sender);
        senderAccount.setBalance(500.0);

        Account receiverAccount = new Account();
        receiverAccount.setUser(receiver);
        receiverAccount.setBalance(200.0);

        String receiverEmail = receiver.getEmail();
        String description = "Paiement test";
        double amount = 100.0;

        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
        when(accountRepository.findByUserId(sender.getId())).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiver.getId())).thenReturn(Optional.of(receiverAccount));

        // Act
        transferService.transfer(sender, receiverEmail, description, amount);

        // Assert
        assertEquals(400.0, senderAccount.getBalance());
        assertEquals(300.0, receiverAccount.getBalance());

        verify(accountRepository).save(senderAccount);
        verify(accountRepository).save(receiverAccount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTx = transactionCaptor.getValue();
        assertEquals(sender, savedTx.getSender());
        assertEquals(receiver, savedTx.getReceiver());
        assertEquals(description, savedTx.getDescription());
        assertEquals(amount, savedTx.getAmount());
    }

    @Test
    void transfer_shouldThrowException_whenReceiverNotFound() {
        User sender = new User();
        sender.setId(1);

        String receiverEmail = "notfound@example.com";

        when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(sender, receiverEmail, "Test", 100.0)
        );

        assertEquals("Destinataire introuvable", ex.getMessage());

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_shouldThrowException_whenSenderAccountNotFound() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(accountRepository.findByUserId(sender.getId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(sender, receiver.getEmail(), "Test", 100.0)
        );

        assertEquals("Compte émetteur introuvable", ex.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_shouldThrowException_whenReceiverAccountNotFound() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        Account senderAccount = new Account();
        senderAccount.setUser(sender);
        senderAccount.setBalance(500.0);

        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(accountRepository.findByUserId(sender.getId())).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiver.getId())).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(sender, receiver.getEmail(), "Test", 100.0)
        );

        assertEquals("Compte destinataire introuvable", ex.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer_shouldThrowException_whenInsufficientBalance() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@example.com");

        Account senderAccount = new Account();
        senderAccount.setUser(sender);
        senderAccount.setBalance(50.0); // insuffisant

        Account receiverAccount = new Account();
        receiverAccount.setUser(receiver);
        receiverAccount.setBalance(200.0);

        when(userRepository.findByEmail(receiver.getEmail())).thenReturn(Optional.of(receiver));
        when(accountRepository.findByUserId(sender.getId())).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByUserId(receiver.getId())).thenReturn(Optional.of(receiverAccount));

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                transferService.transfer(sender, receiver.getEmail(), "Test", 100.0)
        );

        assertEquals("Solde insuffisant !", ex.getMessage());

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}
