package org.stigg.chat.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.stigg.chat.pojo.User;


@Controller
public class MainController {


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Authentication authentication){
        if (authentication != null &&authentication.isAuthenticated()) return "redirect:/chat";

        return "login";
    }

    @GetMapping("/chat")
    public String chat(Authentication authentication, Model model){
        User current_user = (User) authentication.getPrincipal();


        model.addAttribute("user_chats", current_user.getChats());
        model.addAttribute("friends", current_user.getFriends());
        model.addAttribute("user_name", current_user.getUsername());
        return "chat";
    }
}
