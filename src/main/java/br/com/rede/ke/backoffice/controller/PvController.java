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
import java.util.Optional;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
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
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv";
    }

    @PostMapping("/pv")
    public String create(Model model,
                         @RequestParam MultipartFile file,
                         @RequestParam Acquirer acquirer,
                         @RequestParam String email) {

        User user = findOrCreateUser(email);
        try {
            PvBatch pvBatch = pvPermissionService.giveUserPermissionForHeadquarter(
                PvFactory.fromFileAndAcquirer(file, acquirer), user);

            model.addAttribute("userMessage", buildUserMessage(pvBatch));
            model.addAttribute("invalidPvs", pvBatch.getInvalidPvs());
        } catch (IOException e) {
            model.addAttribute("userMessage", buildUserMessage(e));
        }
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv";
    }

    private User findOrCreateUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        return user.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });
    }

    private String buildUserMessage(Exception exception) {
        return String.format("Um erro ocorreu ao fazer upload do arquivo: %s", exception.getMessage());
    }

    private String buildUserMessage(PvBatch pvBatch) {
        String userMessage = "Operação ocorreu com sucesso";

        if (hasInvalidPvs(pvBatch)) {
            userMessage = "Com exceção dos PVs abaixo, a operação ocorreu com sucesso:";
        }
        return userMessage;
    }

    private boolean hasInvalidPvs(PvBatch pvBatch) {
        return pvBatch != null && !pvBatch.getInvalidPvs().isEmpty();
    }
}
