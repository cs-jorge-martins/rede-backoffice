/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : AdminUser.java
 * Descrição: AdminUser.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.authentication.domain.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The Class AdminUser.
 */
@ConfigurationProperties("security.user")
public class AdminUser {

    /** The name. */
    private String name;

    /** The password. */
    private String password;

    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
