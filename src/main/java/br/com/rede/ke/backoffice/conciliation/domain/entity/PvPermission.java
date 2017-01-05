/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermission.java
 * Descrição: PvPermission.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

/**
 * The Class PvPermission.
 */
@Entity
@Table(name = "USER_PV")
public class PvPermission {

    /** The id. */
    @EmbeddedId
    private PvPermissionId id;

    /** The user. */
    @ManyToOne
    @MapsId("userId")
    private User user;

    /** The pv. */
    @ManyToOne
    @MapsId("pvId")
    private Pv pv;

    public PvPermission() {}

    public PvPermission(PvPermissionId id, User user, Pv pv) {
        this.id = id;
        this.user = user;
        this.pv = pv;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public PvPermissionId getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(PvPermissionId id) {
        this.id = id;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the pv.
     *
     * @return the pv
     */
    public Pv getPv() {
        return pv;
    }

    /**
     * Sets the pv.
     *
     * @param pv the new pv
     */
    public void setPv(Pv pv) {
        this.pv = pv;
    }

    public boolean permitAccess(Pv checkPv) {
        return checkPv.equals(getPv()) || getPv().getBranches().stream().anyMatch(pv -> pv.equals(checkPv));
    }
}
