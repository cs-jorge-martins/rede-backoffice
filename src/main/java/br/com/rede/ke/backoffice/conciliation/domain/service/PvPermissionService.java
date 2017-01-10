/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionService.java
 * Descrição: PvPermissionService.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.pvAcquirerEqualTo;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.pvCodeContains;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.userEmailContains;
import static org.springframework.data.jpa.domain.Specifications.not;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.util.StringUtils.isEmpty;
import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.exception.UserNotFoundException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import br.com.rede.ke.backoffice.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermissionId;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The Class PvPermissionService.
 */
@Service
public class PvPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PvPermissionService.class);

    private PvRepository pvRepository;
    private PvService pvService;
    private PvPermissionRepository pvPermissionRepository;
    private UserService userService;

    public PvPermissionService(PvPermissionRepository repository, PvService pvService, PvRepository pvRepository, UserService userService) {
        this.pvPermissionRepository = repository;
        this.pvService = pvService;
        this.pvRepository = pvRepository;
        this.userService = userService;
    }

    /**
     * Find all by acquirer and code and email.
     *
     * @param acquirer the acquirer
     * @param code the code
     * @param email the email
     * @param pageable the pageable
     * @return the page
     */
    public Page<PvPermission> findAllByAcquirerAndCodeAndEmail(Acquirer acquirer, String code, String email,
        Pageable pageable) {
        Specifications<PvPermission> spec = where(not(pvAcquirerEqualTo(Acquirer.REDE)));

        if (!isEmpty(code)) {
            spec = spec.and(pvCodeContains(code));
        }

        if (!Acquirer.NULL.equals(acquirer)) {
            spec = spec.and(pvAcquirerEqualTo(acquirer));
        }

        if (!isEmpty(email)) {
            spec = spec.and(userEmailContains(email));
        }

        return pvPermissionRepository.findAll(spec, pageable);
    }

    public void giveUserPermissionForHeadquarter(List<Pv> pvs, User user) {
        PvBatch pvBatch = pvService.generatePvBatch(pvs);
        this.savePvPermissionsForUser(pvBatch, user);

    }

    /**
     * Creates for secondary User
     * @param pvPermissionRequests list of pv permission request
     * @return the list of pv permission result
     */
    public List<Result<PvPermission, String>> createForSecondaryUser(List<SecondaryUserPvPermissionRequest> pvPermissionRequests) {
        return pvPermissionRequests.stream()
            .map(this::createForSecondaryUser)
            .collect(Collectors.toList());
    }

    /**
     * Create pv permission for secondary user.
     * @param request the secondary user pv permission request.
     */
    public Result<PvPermission, String> createForSecondaryUser(SecondaryUserPvPermissionRequest request) {
        User primaryUser = userService.getPrimaryUser(request.getRequesterUserEmail())
                .orElseThrow(getUserNotFoundException(request.getRequesterUserEmail()));
        User secondaryUser = userService.getSecondaryUserFor(primaryUser, request.getToBePermittedUserEmail())
                .orElseThrow(getUserNotFoundException(request.getToBePermittedUserEmail()));

        Optional<Pv> pvOpt = pvRepository.findByCodeAndAcquirerId(request.getPvCode(), request.getAcquirer().ordinal());

        if (!pvOpt.isPresent()) {
            return Result.failure(String.format("Pv '%s' não existe", request.getPvCode()));
        }

        Pv pv = pvOpt.get();

        if (!userService.hasAccess(primaryUser, pv)) {
            return Result.failure(String.format("Usuário '%s' não tem acesso ao Pv '%s'",
                primaryUser.getEmail(), pv.getCode()));
        }

        PvPermission pvPermission = new PvPermission(secondaryUser, pv);

        pvPermissionRepository.save(pvPermission);
        return Result.success(pvPermission);
    }

    /**
     * Gets user not found exception
     * @param email the user email
     * @return the UserNotFoundException supplier
     */
    private Supplier<UserNotFoundException> getUserNotFoundException(String email) {
        return () -> new UserNotFoundException(String.format("Usuário com email '%s' não encontrado", email));
    }


    public void savePvPermissionsForUser(PvBatch pvBatch, User user){
        for(Pv pv: pvBatch.getValidPvs()){
            Pv savedPv = pvRepository.save(pv);
            PvPermissionId pvPermissionId = new PvPermissionId(user.getId(), savedPv.getId());
            PvPermission pvPermission = new PvPermission(pvPermissionId, user, savedPv);
            pvPermissionRepository.save(pvPermission);
        }
    }
}
