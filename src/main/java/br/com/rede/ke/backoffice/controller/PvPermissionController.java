/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionController.java
 * Descrição: PvPermissionController.java.
 * Autor    : Dayany Espindola <dcortes@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.DomainException;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import br.com.rede.ke.backoffice.conciliation.domain.service.UserService;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import br.com.rede.ke.backoffice.util.Result;

/**
 * The Class PvPermissionController.
 */
@Controller
public class PvPermissionController {

    /** The pv permission service. */
    private PvPermissionService pvPermissionService;

    /** The user service. */
    private UserService userService;

    /**
     * Constructor.
     *
     * @param pvPermissionService
     *            pvPermissionService.
     * @param userService
     *            userService.
     */
    public PvPermissionController(PvPermissionService pvPermissionService, UserService userService) {
        this.pvPermissionService = pvPermissionService;
        this.userService = userService;
    }

    /**
     * Get pv permissions.
     *
     * @param model
     *            page model.
     * @param pageable
     *            the pageable
     * @param code
     *            the code
     * @param acquirer
     *            the acquirer
     * @param email
     *            the email
     * @return mapping.
     */
    @GetMapping({"/", "/pv-permissions"})
    public String index(Model model,
        @PageableDefault(size = 20, sort = {"user.email", "pv.acquirerId", "pv.code"},
        direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false, defaultValue = "") String code,
        @RequestParam(required = false, defaultValue = "NULL") Acquirer acquirer,
        @RequestParam(required = false) String email) {

        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("email", email);
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        Page<PvPermission> pvPermissions = pvPermissionService.findAllByAcquirerAndCodeAndEmail(acquirer, code, email,
            pageable);
        model.addAttribute("pvPermissions", pvPermissions);
        model.addAttribute("title", "Permissões de PVs");
        return "pv-permissions/index";
    }

    /**
     * Primary.
     *
     * @param model
     *            the model
     * @return the string
     */
    @GetMapping("/pv-permissions/primary")
    public String primary(Model model) {
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/primary";
    }

    /**
     * Post pv permissions.
     *
     * @param model
     *            page model.
     * @param file
     *            uploaded file.
     * @param acquirer
     *            acquirer.
     * @param email
     *            user email.
     * @return mapping.
     */
    @PostMapping("/pv-permissions/primary")
    public String create(Model model,
        @RequestParam MultipartFile file,
        @RequestParam Acquirer acquirer,
        @RequestParam String email) {

        try {
            User user = userService.getOrCreatePrimaryUser(email);
            PvBatch pvBatch = pvPermissionService.giveUserPermissionForHeadquarter(
                PvFactory.fromFileAndAcquirer(file, acquirer), user);

            model.addAttribute("validPvs", pvBatch.getValidPvs());
            model.addAttribute("invalidPvs", pvBatch.getInvalidPvs());
        } catch (DomainException | InvalidFileException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/primary";
    }

    /**
     * Gets the secondary.
     *
     * @param model
     *            the model
     * @return the secondary
     */
    @GetMapping("/pv-permissions/secondary")
    public String getSecondary(Model model) {
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/secondary";
    }

    /**
     * Creates the secondary.
     *
     * @param model
     *            the model
     * @param file
     *            the file
     * @param acquirer
     *            the acquirer
     * @param primaryEmail
     *            the primary email
     * @param secondaryEmail
     *            the secondary email
     * @return the string
     */
    @PostMapping("/pv-permissions/secondary")
    public String createSecondary(Model model,
        @RequestParam MultipartFile file,
        @RequestParam Acquirer acquirer,
        @RequestParam String primaryEmail,
        @RequestParam String secondaryEmail) {

        try {

            List<SecondaryUserPvPermissionRequest> pvPermissionRequests = PvFactory.fromFileAndAcquirer(file, acquirer)
                .stream()
                .map(pv -> new SecondaryUserPvPermissionRequest(primaryEmail, secondaryEmail, pv.getCode(), acquirer))
                .collect(Collectors.toList());
            List<Result<PvPermission, String>> results = pvPermissionService
                .createForSecondaryUser(pvPermissionRequests);
            model.addAttribute("validPvs", Result.getSuccessValues(results));
            model.addAttribute("invalidPvs", Result.getFailureValues(results));
        } catch (DomainException | InvalidFileException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("primaryEmail", primaryEmail);
        model.addAttribute("secondaryEmail", secondaryEmail);
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/secondary";
    }
}
