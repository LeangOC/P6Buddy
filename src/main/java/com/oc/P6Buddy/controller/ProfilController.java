package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.model.User;
import com.oc.P6Buddy.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {
        Optional<User> user = authService.authenticate(email, password);
        if (user.isPresent()) {
            User loggedUser = user.get();

            // Stockage en session
            session.setAttribute("loggedUser", loggedUser);

            // Passage au modèle
            model.addAttribute("user", loggedUser);

            // Récupérer le solde
            Double balance = authService.getUserBalance(loggedUser.getId());
            model.addAttribute("balance", balance);

            return "home"; // page d’accueil après connexion
        }

        model.addAttribute("error", "Email ou mot de passe incorrect.");
        model.addAttribute("enteredEmail", email);
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // templates/signup.html
    }

    @PostMapping("/signup")
    public String doSignup(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        try {
            authService.register(username, email, password);
            model.addAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            return "signup";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // Ajout de la route Home
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (loggedUser == null) {
            return "redirect:/login"; // sécurité si pas connecté
        }

        // Passage des données au modèle
        model.addAttribute("user", loggedUser);

        // Recalcul ou récupération du solde
        Double balance = authService.getUserBalance(loggedUser.getId());
        model.addAttribute("balance", balance);

        return "home"; // templates/home.html
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
            User updatedUser = authService.updateUser(loggedUser.getId(), username, email, password);

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
