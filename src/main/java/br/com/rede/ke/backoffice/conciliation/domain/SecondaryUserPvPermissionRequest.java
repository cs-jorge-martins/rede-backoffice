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
public class SecondaryUserPvPermissionRequest extends PrimaryUserPvPermissionRequest{

    /** To be permitted user email */
    private String toBePermittedUserEmail;

    /**
     * Constructor.
     *
     * @param toBePermittedUserEmail child user email param
     */
    public SecondaryUserPvPermissionRequest(String requesterUserEmail, String toBePermittedUserEmail, String pvCode, Acquirer acquirer) {
        super(requesterUserEmail, pvCode, acquirer);
        this.toBePermittedUserEmail = toBePermittedUserEmail;
    }

    /**
     * Gets the to be permitted user email.
     *
     * @return the to be permitted user email
     */
    public String getToBePermittedUserEmail() {
        return toBePermittedUserEmail;
    }
}
