package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.dto.SignupRequestDTO;
import com.oc.P6Buddy.service.SignupService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupRequestDTO", new SignupRequestDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignup(@ModelAttribute("signupRequestDTO") @Valid SignupRequestDTO signupDto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "signup"; // erreurs de validation → on reste sur la page
        }

        try {
            signupService.register(signupDto.getUsername(), signupDto.getEmail(), signupDto.getPassword());
            model.addAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "signup";
    }
}
