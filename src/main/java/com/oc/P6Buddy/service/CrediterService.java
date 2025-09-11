package com.oc.P6Buddy.service;

import com.oc.P6Buddy.model.Account;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.repository.AccountRepository;
import com.oc.P6Buddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrediterService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public CrediterService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public Double getUserBalance(Integer userId) {
        return accountRepository.findByUserId(userId)
                .map(Account::getBalance)
                .orElse(0.0);
    }
    @Transactional
    public void crediterCompte(Integer userId, Double montant) {
        if (montant == null || montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable pour l'utilisateur : " + userId));

        account.setBalance(account.getBalance() + montant);
        accountRepository.save(account);
    }

}
