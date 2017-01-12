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
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.DomainException;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import br.com.rede.ke.backoffice.conciliation.domain.service.UserService;
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
    private UserService userService;

    /**
     * Constructor.
     * @param pvPermissionService pvPermissionService.
     * @param userService userService.
     */
    public PvController(PvPermissionService pvPermissionService, UserService userService) {
        this.pvPermissionService = pvPermissionService;
        this.userService = userService;
    }

    /**
     * Get pv.
     * @param model page model.
     * @return mapping.
     */
    @GetMapping("/pv")
    public String pv(Model model) {
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv";
    }

    /**
     * Post pv.
     * @param model page model.
     * @param file uploaded file.
     * @param acquirer acquirer.
     * @param email user email.
     * @return mapping.
     */
    @PostMapping("/pv")
    public String create(Model model,
                         @RequestParam MultipartFile file,
                         @RequestParam Acquirer acquirer,
                         @RequestParam String email) {

        try {
            User user = userService.getOrCreatePrimaryUser(email);
            PvBatch pvBatch = pvPermissionService.giveUserPermissionForHeadquarter(
                PvFactory.fromFileAndAcquirer(file, acquirer), user);

            model.addAttribute("userMessage", buildUserMessage(pvBatch));
            model.addAttribute("invalidPvs", pvBatch.getInvalidPvs());
        } catch (DomainException e) {
            model.addAttribute("userMessage", buildUserMessage(e));
        } catch (IOException e) {
            model.addAttribute("userMessage", buildInvalidFileMessage());
        }
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv";
    }

    private String buildUserMessage(Exception exception) {
        return String.format("Um erro ocorreu: %s", exception.getMessage());
    }

    private String buildInvalidFileMessage() {
        return "Erro ao processar arquivo";
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
