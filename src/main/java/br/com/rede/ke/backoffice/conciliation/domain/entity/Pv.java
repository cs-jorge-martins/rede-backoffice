/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : Pv.java
 * Descrição: Pv.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Class Pv.
 */
@Entity
@Table(name = "PV")
public class Pv {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    /** The code. */
    @Column(name = "CODE")
    private String code;

    /** The headquarter. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HEADQUARTER_ID")
    private Pv headquarter;

    /** The acquirer id. */
    @Column(name = "ACQUIRER_ID")
    private Integer acquirerId;

    /** The users. */
    @ManyToMany
    @JoinTable(
        name = "USER_PV",
        joinColumns = @JoinColumn(name = "PV_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    private Set<User> users;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param code
     *            the new code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the users.
     *
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Gets the acquirer id.
     *
     * @return the acquirer id
     */
    public Integer getAcquirerId() {
        return acquirerId;
    }

    /**
     * Sets the acquirer id.
     *
     * @param acquirerId
     *            the new acquirer id
     */
    public void setAcquirerId(Integer acquirerId) {
        this.acquirerId = acquirerId;
    }

    /**
     * Sets the users.
     *
     * @param users
     *            the new users
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Gets the acquirer.
     *
     * @return the acquirer
     */
    public Acquirer getAcquirer() {
        return Acquirer.fromId(getAcquirerId());
    }

    /**
     * Checks if is headquarter.
     *
     * @return true, if is headquarter
     */
    public boolean isHeadquarter() {
        return this.headquarter == null;
    }
}
