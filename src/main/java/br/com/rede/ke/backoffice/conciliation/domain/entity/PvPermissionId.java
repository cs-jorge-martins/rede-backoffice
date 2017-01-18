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

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
    
    public PvPermissionId(){}
    
    public PvPermissionId(Long userId, Long pvId){
        this.userId = userId;
        this.pvId = pvId;
    }

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
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, pvId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PvPermissionId pvPermissionId = (PvPermissionId) o;
        return Objects.equals(userId, pvPermissionId.getUserId())
            && Objects.equals(pvId, pvPermissionId.getPvId());
    }
}
