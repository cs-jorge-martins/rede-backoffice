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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    /**
     * The default constructor.
     */
    public PvPermission() {}

    /**
     * The pv permission constructor.
     * @param user the user
     * @param pv the pv
     */
    public PvPermission(User user, Pv pv) {
        this.id = new PvPermissionId(user.getId(), pv.getId());
        this.user = user;
        this.pv = pv;
    }

    /**
     * The pv permission constructor.
     * @param id the id
     * @param user the user
     * @param pv the pv
     */
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

    /**
     * Permit access.
     * @param pv Pv to check access.
     * @return if given pv is permitted.
     */
    public boolean permitAccess(Pv pv) {
        if(getPv() == null || pv == null) {
            return false;
        }

        if(pv.equals(getPv())) {
            return true;
        }

        return Optional.ofNullable(getPv())
            .map(Pv::getBranches)
            .map(List::stream)
            .map(pvStream -> pvStream.anyMatch(pvBranch -> pvBranch.equals(pv)))
            .orElse(false);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PvPermission that = (PvPermission) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(user, that.user) &&
            Objects.equals(pv, that.pv);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, user, pv);
    }
}
