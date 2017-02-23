/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PrimaryUserPvPermissionRequest.java
 * Descrição: PrimaryUserPvPermissionRequest.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 19/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.request;

import java.util.List;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;

/**
 * Class PrimaryUserPvPermissionRequest.
 */
public class PrimaryUserPvPermissionRequest extends PvPermissionRequest {

    /** The pv headquarter rede. */
    protected Pv pvHeadquarterRede;

    /**
     * Constructor.
     *
     * @param requesterUserEmail
     *            user email param
     * @param pvs
     *            list of pvs
     * @param pvHeadquarterRede
     *            the pv headquarter rede
     */
    public PrimaryUserPvPermissionRequest(String requesterUserEmail, List<Pv> pvs, Pv pvHeadquarterRede) {
        super(requesterUserEmail, pvs);
        this.pvHeadquarterRede = pvHeadquarterRede;
    }

    /**
     * Gets the pv headquarter rede.
     *
     * @return the pv headquarter rede
     */
    public Pv getPvHeadquarterRede() {
        return pvHeadquarterRede;
    }

}
