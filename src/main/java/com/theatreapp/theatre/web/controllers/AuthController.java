package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.exceptions.InvalidDataException;
import com.theatreapp.theatre.exceptions.UserAlreadyExistsException;
import com.theatreapp.theatre.services.UserServiceImpl;
import com.theatreapp.theatre.web.models.KeywordViewModel;
import lombok.AllArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@AllArgsConstructor
public class AuthController {
   private final UserServiceImpl userService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping("/register")
    public String viewRegisterUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", " ");

        return "register";
    }

    @GetMapping("/user-entrance")
    public String viewEntranceUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", " ");

        return "user-entrance";
    }

    @GetMapping("/unauthorized")
    public String viewUnauthorizedPage(Model model) {
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", " ");

        return "error-page";
    }

    @PostMapping("/unauthorized")
    public String getUnauthorizedPage(Model model) {
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", " ");

        return "error-page";
    }

    @PostMapping("/register")
    public String registerUser(Model model,
                               @ModelAttribute User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        model.addAttribute("user", new User());
        try {
            userService.registerUser(user);
        } catch (UserAlreadyExistsException | InvalidDataException e) {
            model.addAttribute("keyword", new KeywordViewModel());
            model.addAttribute("message", e.getMessage());
            model.addAttribute("user", new User());

            return "register";
        }
        return "redirect:/login";
    }

    @PostMapping("/update-user")
    public String updateUser(Model model, @ModelAttribute User user) {
        if(this.getUser() != null) {
            try {
                userService.updateUser(user);
                model.addAttribute("message", "Успешна промяна на данни!");
            } catch (InvalidDataException | UserAlreadyExistsException e) {
                model.addAttribute("message", e.getMessage());
            }
        }

        model.addAttribute("user", this.getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("keyword", new KeywordViewModel());

        return "user-profile";
    }

    @PostMapping("/remove-profile")
    public String deleteUser(Model model, @RequestParam String id) {
        if(this.getUser() != null) {
            userService.deleteProfile(id);
        }

        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("user", new User());

        return "redirect:/logout";
    }

    @PreAuthorize("!isAuthenticated()")
    @GetMapping("/login")
    public String loginUser(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("keyword", new KeywordViewModel());

        if(error != null) {
            model.addAttribute("message", "Грешни данни!");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
