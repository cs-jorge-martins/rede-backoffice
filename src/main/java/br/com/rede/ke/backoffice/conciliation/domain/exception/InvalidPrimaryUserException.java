/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : InvalidPrimaryUserException.java
 * Descrição: InvalidPrimaryUserException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

/**
 * The InvalidPrimaryUserException class.
 */
public class InvalidPrimaryUserException extends DomainException {
    /**
     * The user.
     */
    private User user;

    /**
     * Invalid primary user exception constructor.
     * @param user the user.
     */
    public InvalidPrimaryUserException(User user) {
        super(String.format("The user '%s' is a secondary user instead primary user.", user.getEmail()));
        this.user = user;
    }
}
