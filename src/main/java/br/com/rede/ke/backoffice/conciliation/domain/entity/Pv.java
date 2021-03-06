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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Class Pv.
 */
@Entity
@Table(name = "PV", uniqueConstraints = @UniqueConstraint(columnNames = {"CODE", "ACQUIRER_ID"}))
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

    /** the branches set. */
    @OneToMany(mappedBy = "headquarter")
    private List<Pv> branches;

    /** The users. */
    @ManyToMany
    @JoinTable(
        name = "USER_PV",
        joinColumns = @JoinColumn(name = "PV_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    private Set<User> users;

    /**
     * Instantiates a new pv.
     */
    public Pv() {
        this.branches = new ArrayList<>();
    }

    /**
     * Instantiates a new pv.
     *
     * @param code
     *            the code
     */
    public Pv(String code) {
        this();
        this.code = code;
    }

    /**
     * Instantiates a new pv.
     *
     * @param code
     *            the code
     * @param acquirer
     *            the acquirer
     */
    public Pv(String code, Acquirer acquirer) {
        this();
        this.code = code;
        this.acquirerId = acquirer.ordinal();
    }

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

    /**
     * Sets the headquarter.
     *
     * @param headquarter
     *            the new headquarter
     */
    public void setHeadquarter(Pv headquarter) {
        this.headquarter = headquarter;
    }

    /**
     * Gets the headquarter.
     *
     * @return the headquarter
     */
    public Pv getHeadquarter() {
        return headquarter;
    }

    /**
     * Gets the branches.
     *
     * @return the branches
     */
    public List<Pv> getBranches() {
        return branches;
    }

    /**
     * Sets the branches.
     *
     * @param branches
     *            the new branches
     */
    public void setBranches(List<Pv> branches) {
        this.branches = branches;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Pv)) {
            return false;
        }

        Pv pv = (Pv) o;
        return Objects.equals(id, pv.getId())
            && Objects.equals(code, pv.getCode())
            && Objects.equals(headquarter, pv.getHeadquarter())
            && Objects.equals(acquirerId, pv.getAcquirerId());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, code, headquarter, acquirerId);
    }
}
