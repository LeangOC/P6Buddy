package com.oc.P6Buddy.controller;

import com.oc.P6Buddy.dto.SignupRequestDTO;
import com.oc.P6Buddy.service.SignupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SignupControllerTest {

    private SignupService signupService;
    private SignupController signupController;
    private Model model;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        signupService = mock(SignupService.class);
        signupController = new SignupController(signupService);
        model = mock(Model.class);
        bindingResult = mock(BindingResult.class);
    }

    // ---------- TEST GET /signup ----------

    @Test
    void testSignupPageReturnsSignupView() {
        // Act
        String viewName = signupController.signupPage(model);

        // Assert
        assertEquals("signup", viewName);
        verify(model).addAttribute(eq("signupRequestDTO"), any(SignupRequestDTO.class));
    }

    // ---------- TEST POST /signup ----------

    @Test
    void testDoSignupSuccess() {
        // Arrange
        SignupRequestDTO signupDto = new SignupRequestDTO();
        signupDto.setUsername("testuser");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = signupController.doSignup(signupDto, bindingResult, model);

        // Assert
        assertEquals("signup", viewName);
        verify(signupService).register("testuser", "test@example.com", "password");
        verify(model).addAttribute("success", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
    }

    @Test
    void testDoSignupValidationErrors() {
        // Arrange
        SignupRequestDTO signupDto = new SignupRequestDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = signupController.doSignup(signupDto, bindingResult, model);

        // Assert
        assertEquals("signup", viewName);
        verifyNoInteractions(signupService);
        verify(model, never()).addAttribute(eq("success"), any());
    }

    @Test
    void testDoSignupThrowsException() {
        // Arrange
        SignupRequestDTO signupDto = new SignupRequestDTO();
        signupDto.setUsername("testuser");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new IllegalArgumentException("Cet email est déjà utilisé"))
                .when(signupService).register(any(), any(), any());

        // Act
        String viewName = signupController.doSignup(signupDto, bindingResult, model);

        // Assert
        assertEquals("signup", viewName);
        verify(model).addAttribute("error", "Cet email est déjà utilisé");
    }
}
