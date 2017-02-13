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

import java.util.List;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;

/**
 * Class PrimaryUserPvPermissionRequest.
 */
public class PrimaryUserPvPermissionRequest {
    /** Requester user email */
    private String requesterUserEmail;

    /** List of Pvs */
    private List<Pv> pvs;

    /**
     * Constructor.
     *
     * @param requesterUserEmail user email param
     * @param pvs list of pvs
     */
    public PrimaryUserPvPermissionRequest(String requesterUserEmail, List<Pv> pvs) {
        this.requesterUserEmail = requesterUserEmail;
        this.pvs = pvs;
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
     * Gets the pv list.
     *
     * @return the pv list
     */
    public List<Pv> getPvs() {
        return pvs;
    }
}
