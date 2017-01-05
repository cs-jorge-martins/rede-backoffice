/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserService.java
 * Descrição: UserService.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 03/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import org.springframework.stereotype.Service;

/**
 * The UserService class.
 */
@Service
public class UserService {

    private PvPermissionRepository pvPermissionRepository;

    /**
     * Constructor.
     * @param pvPermissionRepository Pv permission repository.
     */
    public UserService(PvPermissionRepository pvPermissionRepository) {
        this.pvPermissionRepository = pvPermissionRepository;
    }

    /**
     * Verify if user has access to pv.
     * @param user user.
     * @param pv pv.
     * @return if has access.
     */
    public boolean hasAccess(User user, Pv pv) {
        return pvPermissionRepository.findByUser(user).stream()
            .anyMatch(pvPermission -> pvPermission.permitAccess(pv));
    }
}
