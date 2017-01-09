/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvNotFoundException.java
 * Descrição: PvNotFoundException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain.exception;

/**
 * The PvNotFoundException class.
 */
public class PvNotFoundException extends DomainException {
    /**
     * The constructor.
     * @param cause error message.
     */
    public PvNotFoundException(String cause) {
        super(cause);
    }
}
