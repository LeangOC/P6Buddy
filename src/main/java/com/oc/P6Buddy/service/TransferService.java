package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.Transaction;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.TransactionRepository;
import com.oc.P6Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(AccountRepository accountRepository,
                           UserRepository userRepository,
                           TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(User sender, String receiverEmail, String description, Double amount) {
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));

        Account senderAccount = accountRepository.findByUserId(sender.getId())
                .orElseThrow(() -> new IllegalArgumentException("Compte émetteur introuvable"));

        Account receiverAccount = accountRepository.findByUserId(receiver.getId())
                .orElseThrow(() -> new IllegalArgumentException("Compte destinataire introuvable"));

        // Vérification solde
        if (senderAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Solde insuffisant !");
        }

        // Débit
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        accountRepository.save(senderAccount);

        // Crédit
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.save(receiverAccount);

        // Enregistrement de la transaction
        Transaction tx = new Transaction();
        tx.setSender(sender);
        tx.setReceiver(receiver);
        tx.setDescription(description);
        tx.setAmount(amount);


        transactionRepository.save(tx);

        // 👉 Si une exception survient après le débit, rollback automatique
    }
}
