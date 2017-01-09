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
     * parent user.
     */
    private User parent;

    /**
     * child user.
     */
    private User child;

    /**
     * setUp test.
     */
    @Before
    public void setUp() {
        this.child = new User();
        this.parent = new User();
        this.child.setPrimaryUser(parent);
    }

    /**
     * Test if is master user.
     */
    @Test
    public void testIsMaster() {
        assertThat(parent.isPrimary(), equalTo(true));
    }

    /**
     * Test if is child user.
     */
    @Test
    public void testIsChild() {
        assertThat(child.isPrimary(), equalTo(false));
    }

    /**
     * Test if is parent of given valid child.
     */
    @Test
    public void testIfIsParentOfGivenValidChild() {
        assertThat(parent.isPrimaryOf(child), equalTo(true));
    }

    /**
     * Test if is parent of given invalid child.
     */
    @Test
    public void testIfIsParentOfGivenInvalidChild() {
        assertThat(child.isPrimaryOf(parent), equalTo(false));
    }
}