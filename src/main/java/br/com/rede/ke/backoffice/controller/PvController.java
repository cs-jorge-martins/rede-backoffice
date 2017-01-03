package br.com.rede.ke.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PvController {
    
    @GetMapping("/pv")
    public String index(Model model){
        model.addAttribute("nome", "joao");
        return "pv";
    }
    
}
