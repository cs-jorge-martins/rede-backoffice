/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionRequest.java
 * Descrição: PvPermissionRequest.java.
 * Autor    : Maitê Balhester <mbalhest@thoughtworks.com>
 * Data     : 22/02/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.request;

import java.util.List;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;

/**
 * The Class PvPermissionRequest.
 */
public abstract class PvPermissionRequest {

    /** Requester user email. */
    private String requesterUserEmail;

    /** List of Pvs. */
    private List<Pv> pvs;

    /**
     * Instantiates a new pv permission request.
     *
     * @param requesterUserEmail
     *            the requester user email
     * @param pvs
     *            the pvs
     */
    public PvPermissionRequest(String requesterUserEmail, List<Pv> pvs) {
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