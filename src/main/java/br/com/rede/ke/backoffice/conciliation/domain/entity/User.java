/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : User.java
 * Descrição: User.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * The Class User.
 */
@Entity
@Table(name = "USER")
public class User {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRIMARY_USER_ID")
    private User primaryUser;

    /** The email. */
    @Column(name = "EMAIL")
    private String email;

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

    public void setPrimaryUser(User primaryUser) {
        this.primaryUser = primaryUser;
    }

    public User getPrimaryUser() {
        return primaryUser;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email
     *            the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPrimary() {
        return primaryUser == null;
    }

    public boolean isPrimaryOf(User secondary) {
        if (secondary == null) {
            return false;
        }
        return this.equals(secondary.getPrimaryUser());
    }

    /**
     * (non-Javadoc)
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(primaryUser, user.primaryUser) &&
                Objects.equals(email, user.email);
    }

    /**
     * (non-Javadoc)
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, primaryUser, email);
    }
}
