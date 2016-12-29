package br.com.rede.ke.backoffice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;

@Controller
public class HomeController {

    private PvPermissionService pvPermissionService;

    public HomeController(PvPermissionService pvPermissionService) {
        this.pvPermissionService = pvPermissionService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 20, sort = {"user.email", "pv.acquirerId", "pv.code" }, direction = Sort.Direction.ASC) Pageable pageable,
                        @RequestParam(required = false, defaultValue = "") String code,
                        @RequestParam(required = false, defaultValue = "NULL") Acquirer acquirer,
                        @RequestParam(required = false) String email) {
        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("email", email);
        model.addAttribute("acquirers", acquirersWithoutRede());
        Page<PvPermission> pvPermissions = pvPermissionService.findAllByAcquirerAndCodeAndEmail(acquirer, code, email, pageable);
        model.addAttribute("pvPermissions", pvPermissions);
        return "home";
    }

    private List<Acquirer> acquirersWithoutRede() {
        return Arrays.asList(Acquirer.values())
               .stream()
               .filter(a -> !Acquirer.REDE.equals(a))
               .collect(Collectors.toList());
    }
}
