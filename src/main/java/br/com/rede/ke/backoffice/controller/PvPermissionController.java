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
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import br.com.rede.ke.backoffice.conciliation.domain.exception.DomainException;
import br.com.rede.ke.backoffice.conciliation.domain.factory.PvFactory;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.request.PrimaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.request.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvPermissionService;
import br.com.rede.ke.backoffice.conciliation.exception.InvalidFileException;
import br.com.rede.ke.backoffice.util.Result;

/**
 * The Class PvPermissionController.
 */
@Controller
public class PvPermissionController {

    /** The pv permission service. */
    private PvPermissionService pvPermissionService;

    /** The pv permission repository. */
    private PvPermissionRepository pvPermissionRepository;

    /** The pv factory. */
    private PvFactory pvFactory;

    /**
     * Instantiates a new pv permission controller.
     *
     * @param pvPermissionService
     *            the pv permission service
     * @param pvPermissionRepository
     *            the pv permission repository
     */
    @SuppressWarnings("unused")
    public PvPermissionController(
        PvPermissionService pvPermissionService,
        PvPermissionRepository pvPermissionRepository,
        PvFactory pvFactory) {
        this.pvPermissionService = pvPermissionService;
        this.pvPermissionRepository = pvPermissionRepository;
        this.pvFactory = pvFactory;
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
        @RequestParam(required = false) String email,
        @RequestParam(required = false, defaultValue = "") String pvHeadquarterRedeCode) {

        model.addAttribute("code", code);
        model.addAttribute("acquirer", acquirer);
        model.addAttribute("email", email);
        model.addAttribute("pvHeadquarterRedeCode", pvHeadquarterRedeCode);
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        Page<PvPermission> pvPermissions = pvPermissionService.findAllByAcquirerAndCodeAndEmailAndPvHeadquarterRede(
            acquirer, code, email,
            pvHeadquarterRedeCode, pageable);
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
        @RequestParam String email,
        @RequestParam String pvHeadquarterRedeCode) {

        try {
            Result<Pv, String> resultPvRede = pvFactory.fromCodeAndAcquirer(pvHeadquarterRedeCode, Acquirer.REDE);

            if (resultPvRede.isFailure()) {
                model.addAttribute("errorMessage", resultPvRede.failure().get());
            } else {
                Pv pvHeadquarterRede = resultPvRede.success().get();

                List<Result<Pv, String>> formatResults = pvFactory.fromFileAndAcquirer(file, acquirer);

                List<Pv> pvs = Result.getSuccessValues(formatResults);

                PrimaryUserPvPermissionRequest pvPermissionRequest = new PrimaryUserPvPermissionRequest(email, pvs,
                    pvHeadquarterRede);

                List<Result<PvPermission, String>> results = pvPermissionService
                    .createForPrimaryUser(pvPermissionRequest);

                List<String> formatFailures = Result.getFailureValues(formatResults);

                List<String> failureMessages = Result.getFailureValues(results);
                failureMessages.addAll(formatFailures);
                model.addAttribute("validPvs", Result.getSuccessValues(results));
                model.addAttribute("invalidPvs", failureMessages);
            }
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
            List<Result<Pv, String>> formatResults = pvFactory.fromFileAndAcquirer(file, acquirer);
            List<Pv> pvs = Result.getSuccessValues(formatResults);

            SecondaryUserPvPermissionRequest pvPermissionRequests = new SecondaryUserPvPermissionRequest(primaryEmail,
                secondaryEmail, pvs);

            List<String> formatFailures = Result.getFailureValues(formatResults);

            List<Result<PvPermission, String>> results = pvPermissionService
                .createForSecondaryUser(pvPermissionRequests);

            List<String> failureMessages = Result.getFailureValues(results);
            failureMessages.addAll(formatFailures);

            model.addAttribute("validPvs", Result.getSuccessValues(results));
            model.addAttribute("invalidPvs", failureMessages);
        } catch (DomainException | InvalidFileException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("primaryEmail", primaryEmail);
        model.addAttribute("secondaryEmail", secondaryEmail);
        model.addAttribute("acquirers", Controllers.acquirersWithoutRede());
        return "pv-permissions/secondary";
    }

    /**
     * Delete.
     *
     * @param model
     *            the model
     * @param pvPermissionIds
     *            the pv permissions
     * @param redirectAttrs
     *            the redirect attrs
     * @return the string
     */
    @DeleteMapping("/pv-permissions")
    public String delete(Model model,
        @RequestParam(required = false) List<PvPermissionId> pvPermissionIds,
        RedirectAttributes redirectAttrs) {
        if (Objects.isNull(pvPermissionIds)) {
            redirectAttrs.addFlashAttribute("message", "selecione pelo menos uma permissão para remover.");
            return "redirect:/pv-permissions";
        }
        List<PvPermission> pvPermissions = pvPermissionRepository.findAll(pvPermissionIds);
        pvPermissions.stream().forEach(pvPermissionService::delete);
        redirectAttrs.addFlashAttribute("message", "os pvs foram removidos com sucesso.");
        return "redirect:/pv-permissions";
    }
}
