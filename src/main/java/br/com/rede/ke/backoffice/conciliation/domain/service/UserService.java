/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : UserService.java
 * Descrição: UserService.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 03/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.Optional;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidPrimaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidSecondaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * The UserService class.
 */
@Service
public class UserService {

    /** The pv permission repository. */
    private PvPermissionRepository pvPermissionRepository;

    /** The user repository. */
    private UserRepository userRepository;

    /**
     * Constructor.
     * @param pvPermissionRepository Pv permission repository.
     * @param userRepository User repository.
     */
    public UserService(PvPermissionRepository pvPermissionRepository, UserRepository userRepository) {
        this.pvPermissionRepository = pvPermissionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Verify if user has access to pv.
     * @param user user.
     * @param pv pv.
     * @return if has access.
     */
    public boolean hasAccess(User user, Pv pv) {
        return pvPermissionRepository.findByUser(user).stream()
            .anyMatch(pvPermission -> pvPermission.permitAccess(pv));
    }

    /**
     * Gets the primary user
     *
     * @param email the user email
     * @return the primary user
     */
    public Optional<User> getPrimaryUser(String email) {
        Optional<User> primaryUserOpt = userRepository.findByEmail(email);

        if (!primaryUserOpt.isPresent()) {
            return Optional.empty();
        }

        User primaryUser = primaryUserOpt.get();
        if (!primaryUser.isPrimary()) {
            throw new InvalidPrimaryUserException(primaryUser);
        }
        return primaryUserOpt;
    }

    /**
     * Gets or creates user email.
     * @param email user email.
     * @return the primary user created or from database.
     */
    public User getOrCreatePrimaryUser(final String email) {
        Optional<User> primaryUserOpt = userRepository.findByEmail(email);

        return primaryUserOpt.map(user -> {
            if (!user.isPrimary()) {
                throw new InvalidPrimaryUserException(user);
            }
            return user;
        }).orElseGet(() -> {
            User primaryUser = new User();
            primaryUser.setEmail(email);
            return userRepository.save(primaryUser);
        });
    }

    /**
     * Gets the secondary user for given primary user.
     *
     * @param primaryUser the primary user
     * @param secondaryUserEmail the secondary user email
     * @return the secondary user
     */
    public Optional<User> getSecondaryUserFor(User primaryUser, String secondaryUserEmail) {
        Optional<User> secondaryUserOpt = userRepository.findByEmail(secondaryUserEmail);

        if (!secondaryUserOpt.isPresent()) {
            return Optional.empty();
        }

        User secondaryUser = secondaryUserOpt.get();
        if (secondaryUser.isPrimary()) {
            throw new InvalidSecondaryUserException(secondaryUser);
        }

        if (!primaryUser.isPrimaryOf(secondaryUser)) {
            throw new InvalidSecondaryUserException(primaryUser, secondaryUser);
        }

        return secondaryUserOpt;
    }
}
