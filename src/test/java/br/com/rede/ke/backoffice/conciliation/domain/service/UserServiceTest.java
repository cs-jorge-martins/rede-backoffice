/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserServiceTest.java
 * Descrição: UserServiceTest.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 03/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidPrimaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidSecondaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;

/**
 * The UserServiceTest.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    /** The user service. */
    @InjectMocks
    private UserService userService;

    /** The pv permission repository. */
    @Mock
    private PvPermissionRepository pvPermissionRepository;

    /** The user repository. */
    @Mock
    private UserRepository userRepository;

    /** The user. */
    private User user;
    
    /** The pv. */
    private Pv pv;
    
    
    /** The Constant PRIMARY_USER_EMAIL. */
    private static final String PRIMARY_USER_EMAIL = "primary_user@email.com";
    
    /** The Constant SECONDARY_USER_EMAIL. */
    private static final String SECONDARY_USER_EMAIL = "secondary_user@email.com";

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        user = new User();
        pv = new Pv();
    }

    /**
     * Has access when user has no permission.
     */
    @Test
    public void hasAccessWhenUserHasNoPermission() {
        when(pvPermissionRepository.findByUser(user)).thenReturn(Collections.emptyList());

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(false));
    }

    /**
     * Has access when user has permission that not allow pv access.
     */
    @Test
    public void hasAccessWhenUserHasPermissionThatNotAllowPvAccess() {
        PvPermission pvPermission = mock(PvPermission.class);
        when(pvPermission.permitAccess(pv)).thenReturn(false);
        when(pvPermissionRepository.findByUser(user)).thenReturn(Arrays.asList(pvPermission));

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(false));
    }

    /**
     * Has access when user has permission that allow pv access.
     */
    @Test
    public void hasAccessWhenUserHasPermissionThatAllowPvAccess() {
        PvPermission pvPermission = mock(PvPermission.class);
        when(pvPermission.permitAccess(pv)).thenReturn(true);
        when(pvPermissionRepository.findByUser(user)).thenReturn(Arrays.asList(pvPermission));

        boolean hasAccess = userService.hasAccess(user, pv);
        assertThat(hasAccess, equalTo(true));
    }

    /**
     * Test get primary user when email belongs to secondary user.
     */
    @Test(expected = InvalidPrimaryUserException.class)
    public void testGetPrimaryUserWhenEmailBelongsToSecondaryUser() {

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(new User());

        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        userService.getPrimaryUser(SECONDARY_USER_EMAIL);
    }

    /**
     * Test get or create primary user when no user exists with this email.
     */
    @Test
    public void testGetOrCreatePrimaryUserWhenNoUserExists() {
        final String primaryUserEmail = "primary_user@email.com";

        User primaryUser = new User();
        primaryUser.setEmail(primaryUserEmail);

        when(userRepository.findByEmail(primaryUserEmail)).thenReturn(Optional.empty());

        userService.getOrCreatePrimaryUser(primaryUserEmail);

        verify(userRepository, times(1)).save(primaryUser);
    }

    /**
     * Test get or create primary user when primary user exists.
     */
    @Test
    public void testGetOrCreatePrimaryUserWhenPrimaryUserAlreadyExists() {
        final String primaryUserEmail = "primary_user@email.com";

        User primaryUser = new User();
        primaryUser.setEmail(primaryUserEmail);

        when(userRepository.findByEmail(primaryUserEmail)).thenReturn(Optional.of(primaryUser));

        User primaryUserFromDb = userService.getOrCreatePrimaryUser(primaryUserEmail);

        verify(userRepository, times(0)).save(primaryUser);
        assertThat(primaryUserFromDb, equalTo(primaryUser));
    }

    /**
     * Test get or create primary user when user already exists and is not a primary user.
     */
    @Test(expected = InvalidPrimaryUserException.class)
    public void testGetOrCreatePrimaryUserWhenUserAlreadyExistsAndIsNotPrimary() {
        final String secondaryUserEmail = "secondary_user@email.com";

        User secondaryUser = new User();
        secondaryUser.setEmail(secondaryUserEmail);
        secondaryUser.setPrimaryUser(new User());

        when(userRepository.findByEmail(secondaryUserEmail)).thenReturn(Optional.of(secondaryUser));

        userService.getOrCreatePrimaryUser(secondaryUserEmail);
    }

    /**
     * Test get secondary user when secondary email belongs to primary user.
     */
    @Test(expected = InvalidSecondaryUserException.class)
    public void testGetSecondaryUserWhenSecondaryEmailBelongsToPrimaryUser() {
        User primaryUser1 = new User();
        User primaryUser2 = new User();

        when(userRepository.findByEmail(PRIMARY_USER_EMAIL)).thenReturn(Optional.of(primaryUser2));

        userService.getOrCreateSecondaryUserFor(primaryUser1, PRIMARY_USER_EMAIL);
    }

    /**
     * Test get secondary user when secondary user doesn't belong to given primary user.
     */
    @Test(expected = InvalidSecondaryUserException.class)
    public void testGetSecondaryUserWhenSecondaryUserDoesntBelongToGivenPrimaryUser() {

        User primaryUser = new User();
        primaryUser.setId(1L);

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(new User());

        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.of(secondaryUser));

        userService.getOrCreateSecondaryUserFor(primaryUser, SECONDARY_USER_EMAIL);
    }
    
    /**
     * Test get or create secondary user when primary user isn't primary at all.
     */
    @Test(expected=InvalidPrimaryUserException.class)
    public void testGetOrCreateSecondaryUserWhenPrimaryUserIsntPrimary() {
        User primaryUser = new User();
        primaryUser.setId(1L);

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        secondaryUser.setEmail(SECONDARY_USER_EMAIL);

        userService.getOrCreateSecondaryUserFor(secondaryUser, SECONDARY_USER_EMAIL);
    }
    
    
    /**
     * Test get or create secondary user when secondary user doesn't exist.
     */
    @Test
    public void testGetOrCreateSecondaryUserWhenSecondaryUserDoesntExist() {
        User primaryUser = new User();
        primaryUser.setId(1L);

        User secondaryUser = new User();
        secondaryUser.setPrimaryUser(primaryUser);
        secondaryUser.setEmail(SECONDARY_USER_EMAIL);

        when(userRepository.findByEmail(SECONDARY_USER_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(secondaryUser)).thenReturn(secondaryUser);
        
        User user = userService.getOrCreateSecondaryUserFor(primaryUser, SECONDARY_USER_EMAIL);
        assertThat(user, equalTo(secondaryUser));
    }
}