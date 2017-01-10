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

    public void setRequesterUserEmail(String requesterUserEmail) {
        this.requesterUserEmail = requesterUserEmail;
    }

    public String getToBePermittedUserEmail() {
        return toBePermittedUserEmail;
    }

    public void setToBePermittedUserEmail(String toBePermittedUserEmail) {
        this.toBePermittedUserEmail = toBePermittedUserEmail;
    }

    public String getPvCode() {
        return pvCode;
    }

    public void setPvCode(String pvId) {
        this.pvCode = pvId;
    }
}
