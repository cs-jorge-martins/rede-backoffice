package br.com.rede.ke.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthenticationController {

    @GetMapping("auth/login")
    public String login(Model model,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout) {

        if (error != null) {
            model.addAttribute("errorMessage", "User name or password invalid!");
        }

        return "auth/login";
    }
}
