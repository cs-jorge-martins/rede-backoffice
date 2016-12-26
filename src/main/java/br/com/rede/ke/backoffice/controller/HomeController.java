package br.com.rede.ke.backoffice.controller;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private PvService pvService;

    public HomeController(PvService pvService) {
        this.pvService = pvService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 20) Pageable pageable,
                        @RequestParam(required = false, defaultValue = "") String code,
                        @RequestParam(required = false, defaultValue = "NULL") Acquirer acquirer,
                        @RequestParam(required = false) String email) {
        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("email", email);
        Page<Pv> pvs = pvService.findAllByAcquirerAndCodeAndUserEmail(acquirer, code, email, pageable);
        model.addAttribute("pvs", pvs);
        return "home";
    }
}
