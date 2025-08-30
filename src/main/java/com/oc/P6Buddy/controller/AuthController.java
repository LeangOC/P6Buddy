package com.oc.P6Buddy.controller;


import com.oc.P6Buddy.model.Users;
import com.oc.P6Buddy.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
                          Model model) {
        Optional<Users> user = authService.authenticate(email, password);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "success"; // templates/success.html
        }
        model.addAttribute("error", "Email ou mot de passe incorrect.");
        model.addAttribute("enteredEmail", email);
        return "login";
    }
}