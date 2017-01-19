/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : SecondaryUserPvPermissionRequest.java
 * Descrição: SecondaryUserPvPermissionRequest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */

package br.com.rede.ke.backoffice.conciliation.domain;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;

/**
 * Secondary user pv permission request.
 */
public class SecondaryUserPvPermissionRequest {

    private String requesterUserEmail;
    private String toBePermittedUserEmail;
    private String pvCode;
    private Acquirer acquirer;

    /**
     * Instantiates a new secondary user pv permission request.
     *
     * @param requesterUserEmail the requester user email
     * @param toBePermittedUserEmail the to be permitted user email
     * @param pvCode the pv code
     * @param acquirer the acquirer
     */
    public SecondaryUserPvPermissionRequest(String requesterUserEmail, String toBePermittedUserEmail, String pvCode,
        Acquirer acquirer) {
        this.requesterUserEmail = requesterUserEmail;
        this.toBePermittedUserEmail = toBePermittedUserEmail;
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
     * Gets the to be permitted user email.
     *
     * @return the to be permitted user email
     */
    public String getToBePermittedUserEmail() {
        return toBePermittedUserEmail;
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
