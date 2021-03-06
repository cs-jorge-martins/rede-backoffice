/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : InvalidSecondaryUserException.java
 * Descrição: InvalidSecondaryUserException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

/**
 * The InvalidSecondaryUserException class.
 */
@SuppressWarnings("serial")
public class InvalidSecondaryUserException extends DomainException {

    /**
     * The constructor.
     * @param secondaryUser the secondary user.
     * @param primaryUser the primary user.
     */
    public InvalidSecondaryUserException(User secondaryUser, User primaryUser) {
        super(String.format("Usuario '%s' não é secundario do usuario '%s'",
            secondaryUser.getEmail(), primaryUser.getEmail()));
    }

    /**
     * The constructor.
     * @param primaryUser the primary user.
     */
    public InvalidSecondaryUserException(User primaryUser) {
        super(String.format("Usuario '%s' é um usuario primario.", primaryUser.getEmail()));
    }
}
