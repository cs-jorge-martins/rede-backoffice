/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : Acquirer.java
 * Descrição: Acquirer.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

/**
 * The Enum Acquirer.
 */
public enum Acquirer {

    /** The null. */
    NULL,

    /** The rede. */
    REDE,

    /** The cielo. */
    CIELO;

    /**
     * From id.
     *
     * @param id
     *            the id
     * @return the acquirer
     */
    static Acquirer fromId(int id) {
        return Acquirer.values()[id];
    }
}
