/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvController.java
 * Descrição: PvController.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.controller;

import java.io.IOException;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * The PvController class.
 */
@Controller
public class PvController {

    private PvPermissionService pvPermissionService;
    private UserRepository userRepository;

    public PvController(PvPermissionService pvPermissionService, UserRepository userRepository) {
        this.pvPermissionService = pvPermissionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/pv")
    public String pv(Model model) {
        model.addAttribute("acquirers", ControllersUtil.acquirersWithoutRede());
        return "pv";
    }

    @PostMapping("/pv")
    public String create(@RequestParam() MultipartFile file,
                         @RequestParam() Acquirer acquirer,
                         @RequestParam() String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            try {
                pvPermissionService.giveUserPermissionForHeadquarter(PvFactory.fromCodesAndAcquirer(file, acquirer), user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "pv";
    }
}
