/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserTest.java
 * Descrição: UserTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 09/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.entity;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

/**
 * The UserTest class.
 */
public class UserTest {

    /**
     * primary user.
     */
    private User primaryUser;

    /**
     * secondary user.
     */
    private User secondaryUser;

    /**
     * setUp test.
     */
    @Before
    public void setUp() {
        this.secondaryUser = new User();
        this.primaryUser = new User();
        this.secondaryUser.setPrimaryUser(primaryUser);
    }

    /**
     * Test if is primary user.
     */
    @Test
    public void testIsPrimary() {
        assertThat(primaryUser.isPrimary(), equalTo(true));
    }

    /**
     * Test if is secondary user.
     */
    @Test
    public void testIsSecondary() {
        assertThat(secondaryUser.isPrimary(), equalTo(false));
    }

    /**
     * Test if is primaryUser of given valid secondaryUser.
     */
    @Test
    public void testIfIsPrimaryOfGivenValidSecondary() {
        assertThat(primaryUser.isPrimaryOf(secondaryUser), equalTo(true));
    }

    /**
     * Test if is primaryUser of given invalid secondaryUser.
     */
    @Test
    public void testIfIsPrimaryOfGivenInvalidSecondary() {
        assertThat(secondaryUser.isPrimaryOf(primaryUser), equalTo(false));
    }
}