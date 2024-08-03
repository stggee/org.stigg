package com.stigg.lernwortspiel.controllers;

import com.stigg.lernwortspiel.dto.UserRegisterDTO;
import com.stigg.lernwortspiel.services.EmailService;
import com.stigg.lernwortspiel.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/app")
public class LoginRegisterController {

    private final FindByIndexNameSessionRepository<? extends Session> findByIndexNameSessionRepository;
    private final UserService userService;

    @GetMapping("/")
    public String index(){
        return "home";
    }

    @GetMapping("/home")
    public String home() {

        return "home";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("new_user", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("new_user") UserRegisterDTO userRegisterDTO) {

        userService.registerUser(userRegisterDTO);
        return "redirect:/login?registered";
    }

    @GetMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String token) {

        System.out.println("token: " + token);
        userService.confirmUser(token);

        return "login";
    }
}
