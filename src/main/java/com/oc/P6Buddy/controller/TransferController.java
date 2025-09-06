package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.Transaction;
import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.TransferService;
import com.oc.P6Buddy.repository.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransferController {

    private final TransferService transferService;
    private final TransactionRepository transactionRepository;

    public TransferController(TransferService transferService,
                              TransactionRepository transactionRepository) {
        this.transferService = transferService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/transfer")
    public String transferPage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        List<Transaction> transactions = transactionRepository.findBySender(loggedUser);
        model.addAttribute("transactions", transactions);

        return "transfer"; // templates/transfer.html
    }

    @PostMapping("/transfer")
    public String doTransfer(@RequestParam String receiverEmail,
                             @RequestParam String description,
                             @RequestParam Double amount,
                             HttpSession session,
                             Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        try {
            transferService.transfer(loggedUser, receiverEmail, description, amount);
            model.addAttribute("success", "Paiement effectué avec succès !");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        List<Transaction> transactions = transactionRepository.findBySender(loggedUser);
        model.addAttribute("transactions", transactions);

        return "transfer";
    }
}
