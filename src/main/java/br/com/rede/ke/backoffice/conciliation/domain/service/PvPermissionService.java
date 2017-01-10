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
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidSecondaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.SecondaryUserPvPermissionRequest;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidPrimaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.UserHasNoPvAccessException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.PvNotFoundException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.UserNotFoundException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
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
    private UserRepository userRepository;

    public PvPermissionService(PvPermissionRepository repository, PvService pvService, PvRepository pvRepository, UserService userService, UserRepository userRepository) {
        this.pvPermissionRepository = repository;
        this.pvService = pvService;
        this.pvRepository = pvRepository;
        this.userService = userService;
        this.userRepository = userRepository;
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

    /**
     * Create pv permission for secondary user.
     * @param pvPermissionRequest the secondary user pv permission request.
     */
    public void createForSecondaryUser(SecondaryUserPvPermissionRequest pvPermissionRequest) {
        User primaryUser = getPrimaryUser(pvPermissionRequest.getRequesterUserEmail());
        User secondaryUser = getSecondaryUser(pvPermissionRequest.getToBePermittedUserEmail());
        Pv pv = getPv(pvPermissionRequest.getPvCode());

        if (!primaryUser.isPrimaryOf(secondaryUser)) {
            throw new InvalidSecondaryUserException(secondaryUser, primaryUser);
        }

        if (!userService.hasAccess(primaryUser, pv)) {
            throw new UserHasNoPvAccessException(primaryUser, pv);
        }

        PvPermission pvPermission = new PvPermission();
        pvPermission.setPv(pv);
        pvPermission.setUser(secondaryUser);

        pvPermissionRepository.save(pvPermission);
    }

    /**
     * Get pv by code.
     * @param code the pv code.
     * @return found pv if exists.
     */
    private Pv getPv(String code) {
        return pvRepository.findByCode(code).orElseThrow(() -> {
            final String cause = String.format("Pv with code '%s' not found!", code);
            LOGGER.warn(cause);
            return new PvNotFoundException(cause);
        });
    }

    /**
     * Get user by email.
     * @param email user email.
     * @return found user if exists.
     */
    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            final String cause = String.format("User with email '%s' not found!", email);
            LOGGER.warn(cause);
            return new UserNotFoundException(cause);
        });
    }

    /**
     * Get primary user.
     * @param email the primary user email.
     * @return found user if exists.
     */
    private User getPrimaryUser(String email) {
        User user = getUser(email);
        if (!user.isPrimary()) {
            throw new InvalidPrimaryUserException(user);
        }
        return user;
    }

    /**
     * Get secondary user.
     * @param email the primary user email.
     * @return found user if exists.
     */
    private User getSecondaryUser(String email) {
        User user = getUser(email);
        if (user.isPrimary()) {
            throw new InvalidSecondaryUserException(user);
        }
        return user;
    }
    
    public void savePvPermissionsForUser(PvBatch pvBatch, User user){
        for(Pv pv: pvBatch.getValidPvs()){
            Pv savedPv = pvService.save(pv);
            PvPermissionId pvPermissionId = new PvPermissionId(user.getId(), savedPv.getId());
            PvPermission pvPermission = new PvPermission(pvPermissionId, user, savedPv);
            pvPermissionRepository.save(pvPermission);
        }
    }
}
