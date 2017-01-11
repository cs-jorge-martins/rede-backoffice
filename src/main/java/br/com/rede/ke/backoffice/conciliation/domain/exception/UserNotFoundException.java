/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserNotFoundException.java
 * Descrição: UserNotFoundException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

/**
 * The UserNotFoundException class.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * The constructor.
     * @param cause the cause message
     */
    public UserNotFoundException(String cause) {
        super(cause);
    }
}
