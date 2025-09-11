package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.CrediterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CrediterController {

    private final CrediterService crediterService;
    public CrediterController(CrediterService crediterService) {
        this.crediterService = crediterService;
    }


    // Ajout de la route Home
    @GetMapping("/crediter")
    public String home(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login"; // sécurité si pas connecté
        }

        // Passage des données au modèle
        model.addAttribute("user", loggedUser);

        // Recalcul ou récupération du solde
        Double balance = crediterService.getUserBalance(loggedUser.getId());
        model.addAttribute("balance", balance);

        return "crediter"; // templates/home.html
    }

    @PostMapping("/crediter")
    public String crediter(@RequestParam("montant") Double montant,
                           HttpSession session,
                           Model model) {

        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        try {
            crediterService.crediterCompte(loggedUser.getId(), montant);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        // Mise à jour du solde
        Double balance = crediterService.getUserBalance(loggedUser.getId());
        model.addAttribute("balance", balance);
        model.addAttribute("user", loggedUser);

        return "crediter";
    }
}
