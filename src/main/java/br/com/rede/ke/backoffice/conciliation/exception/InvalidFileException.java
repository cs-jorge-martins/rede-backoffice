/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : InvalidFileException.java
 * Descrição: InvalidFileException.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 17/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.exception;

import java.io.IOException;

/**
 * The class InvalidFileException.
 */
public class InvalidFileException extends IOException {

    /**
     * The constructor.
     * @param message error message.
     */
    public InvalidFileException(String message) {
        super(message);
    }
}
