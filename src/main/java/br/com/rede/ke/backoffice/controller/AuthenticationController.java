/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : AuthenticationController.java
 * Descrição: AuthenticationController.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The Class AuthenticationController.
 */
@Controller
public class AuthenticationController {

    /**
     * Login.
     *
     * @param model the model
     * @param error the error
     * @param logout the logout
     * @return the string
     */
    @GetMapping("auth/login")
    public String login(Model model,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String logout) {

        if (error != null) {
            model.addAttribute("errorMessage", "nome de usuário ou senha inválidos");
        }

        return "auth/login";
    }
}
