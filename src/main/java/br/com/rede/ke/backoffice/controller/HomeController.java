package br.com.rede.ke.backoffice.controller;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.where;


@Controller
public class HomeController {

    private PvRepository pvRepository;

    public HomeController(PvRepository pvRepository) {
        this.pvRepository = pvRepository;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault Pageable pageable,
                        @RequestParam(required = false) String code,
                        @RequestParam(required = false, defaultValue = "REDE") Acquirer acquirer) {
        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("pvs", pvRepository.findAll(where(pvCodeEqualTo(code)).and(pvAcquirerEqualTo(acquirer)), pageable));
        return "home";
    }
}
