/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserHasNoPvAccessException.java
 * Descrição: UserHasNoPvAccessException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

/**
 * The UserHasNoPvAccessException class.
 */
public class UserHasNoPvAccessException extends DomainException {

    private final User user;
    private final Pv pv;

    /**
     * The constructor.
     * @param user the user to access pv.
     * @param pv the pv to access.
     */
    public UserHasNoPvAccessException(User user, Pv pv) {
        super(String.format("User '%s' has no access to Pv '%s'.", user.getEmail(), pv.getCode()));
        this.user = user;
        this.pv = pv;
    }
}
