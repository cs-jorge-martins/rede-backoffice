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

/**
 * Secondary user pv permission request.
 */
public class SecondaryUserPvPermissionRequest {

    /**
     * Requester user email
     */
    private String requesterUserEmail;

    /**
     * To be permitted user email
     */
    private String toBePermittedUserEmail;

    /**
     * pv id
     */
    private String pvCode;

    /**
     * Constructor.
     *
     * @param requesterUserEmail user user email param
     * @param toBePermittedUserEmail child user email param
     * @param pvCode pv code
     */
    public SecondaryUserPvPermissionRequest(String requesterUserEmail, String toBePermittedUserEmail, String pvCode) {
        this.requesterUserEmail = requesterUserEmail;
        this.toBePermittedUserEmail = toBePermittedUserEmail;
        this.pvCode = pvCode;
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
}
