/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionId.java
 * Descrição: PvPermissionId.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The Class PvPermissionId.
 */
@Embeddable
public class PvPermissionId implements Serializable {

    /** The user id. */
    @Column(name = "USER_ID")
    private Long userId;

    /** The pv id. */
    @Column(name = "PV_ID")
    private Long pvId;

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the pv id.
     *
     * @return the pv id
     */
    public Long getPvId() {
        return pvId;
    }

    /**
     * Sets the pv id.
     *
     * @param pvId the new pv id
     */
    public void setPvId(Long pvId) {
        this.pvId = pvId;
    }
}
