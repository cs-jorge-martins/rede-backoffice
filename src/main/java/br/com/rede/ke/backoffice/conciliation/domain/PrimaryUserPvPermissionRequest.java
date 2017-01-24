/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PrimaryUserPvPermissionRequest.java
 * Descrição: PrimaryUserPvPermissionRequest.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;

/**
 * Class PrimaryUserPvPermissionRequest.
 */
public class PrimaryUserPvPermissionRequest {
    /** Requester user email */
    private String requesterUserEmail;

    /** Pv code */
    private String pvCode;

    /** Acquirer */
    private Acquirer acquirer;

    /**
     * Constructor.
     *
     * @param requesterUserEmail user user email param
     * @param pvCode pv code
     * @param acquirer acquirer
     */
    public PrimaryUserPvPermissionRequest(String requesterUserEmail, String pvCode, Acquirer acquirer) {
        this.requesterUserEmail = requesterUserEmail;
        this.pvCode = pvCode;
        this.acquirer = acquirer;
    }

    /**
     * Gets the requester user email.
     *
     * @return the requester user email
     */
    public String getRequesterUserEmail() {
        return requesterUserEmail;
    }

    /**
     * Gets the pv code.
     *
     * @return the pv code
     */
    public String getPvCode() {
        return pvCode;
    }

    /**
     * Gets acquirer.
     *
     * @return the acquirer
     */
    public Acquirer getAcquirer() {
        return acquirer;
    }
}
