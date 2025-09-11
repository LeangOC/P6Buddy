package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class ProfileController {
    private final ProfileService profileService;
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedUser);
        return "profile"; // templates/profile.html
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        try {
            // Appel du service pour mettre à jour en BDD
            User updatedUser = profileService.updateUser(loggedUser.getId(), username, email, password);

            // Mettre à jour en session
            session.setAttribute("loggedUser", updatedUser);

            model.addAttribute("user", updatedUser);
            model.addAttribute("success", "Profil mis à jour avec succès !");
        } catch (IllegalArgumentException e) {
            model.addAttribute("user", loggedUser);
            model.addAttribute("error", e.getMessage());
        }

        return "profile";
    }

}
