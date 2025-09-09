// com.oc.P6Buddy.controller.RelationController.java
package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.RelationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
public class RelationController {

    private final RelationService relationService;

    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @GetMapping("/relation")
    public String relationPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser == null) {
            return "redirect:/login"; // sécurité si pas connecté
        }

        // Charger directement la liste des relations existantes
        model.addAttribute("relations", relationService.getBuddyEmails(currentUser));

        return "relation"; // templates/relation.html
    }

    @PostMapping("/relation")
    public String addRelation(@RequestParam String email,
                              jakarta.servlet.http.HttpSession session,
                              Model model) {
        User currentUser = (User) session.getAttribute("loggedUser");
        if (currentUser == null) {
            return "redirect:/login"; // sécurité
        }
        String message = relationService.addRelation(currentUser, email);
        model.addAttribute("message", message);

        //Recharger la liste des relations après ajout
        model.addAttribute("relations", relationService.getBuddyEmails(currentUser));

        return "relation";
    }
}