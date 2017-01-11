/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : DomainException.java
 * Descrição: DomainException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 10/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

/**
 * The DomainException class.
 */
public class DomainException extends RuntimeException {

    /**
     * The constructor.
     * @param message the error message
     */
    public DomainException(String message) {
        super(message);
    }
}
