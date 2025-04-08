package db6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import db6.dto.UserRegistrationRequest;
import db6.service.AppUserService;

@Controller
public class LoginController {

    @Autowired
    private AppUserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(UserRegistrationRequest request, Model model) {
        // Ensure email isn't taken
        if (userService.isEmailTaken(request.getEmail())) {
            model.addAttribute("errorMessage", "Email is already taken.");
            return "signup";
        }

        // Ensure password is correct
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            return "signup";
        }

        userService.registerUser(request);
        return "redirect:/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(Model model) {
        model.addAttribute("errorMessage", "Invalid username or password.");
        return "login";
    }

}
