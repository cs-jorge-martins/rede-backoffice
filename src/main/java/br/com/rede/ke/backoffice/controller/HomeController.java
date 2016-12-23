package br.com.rede.ke.backoffice.controller;


import br.com.rede.ke.backoffice.conciliador.domain.repository.PvRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    private PvRepository pvRepository;

    public HomeController(PvRepository pvRepository) {
        this.pvRepository = pvRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pvs", pvRepository.findAll());
        return "home";
    }
}
