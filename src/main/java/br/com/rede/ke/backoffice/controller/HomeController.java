/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : HomeController.java
 * Descrição: HomeController.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
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

/**
 * The Class HomeController.
 */
@Controller
public class HomeController {

    /** The pv permission service. */
    private PvPermissionService pvPermissionService;

    /**
     * Instantiates a new home controller.
     *
     * @param pvPermissionService the pv permission service
     */
    public HomeController(PvPermissionService pvPermissionService) {
        this.pvPermissionService = pvPermissionService;
    }

    /**
     * Index.
     *
     * @param model the model
     * @param pageable the pageable
     * @param code the code
     * @param acquirer the acquirer
     * @param email the email
     * @return the string
     */
    @GetMapping("/")
    public String index(Model model,
        @PageableDefault(size = 20, sort = {"user.email", "pv.acquirerId", "pv.code"},
        direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false, defaultValue = "") String code,
        @RequestParam(required = false, defaultValue = "NULL") Acquirer acquirer,
        @RequestParam(required = false) String email) {
        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("email", email);
        model.addAttribute("acquirers", acquirersWithoutRede());
        Page<PvPermission> pvPermissions = pvPermissionService.findAllByAcquirerAndCodeAndEmail(acquirer, code, email,
            pageable);
        model.addAttribute("pvPermissions", pvPermissions);
        return "home";
    }

    /**
     * Acquirers without rede.
     *
     * @return the list
     */
    private List<Acquirer> acquirersWithoutRede() {
        return Arrays.asList(Acquirer.values())
            .stream()
            .filter(a -> !Acquirer.REDE.equals(a))
            .collect(Collectors.toList());
    }
}
