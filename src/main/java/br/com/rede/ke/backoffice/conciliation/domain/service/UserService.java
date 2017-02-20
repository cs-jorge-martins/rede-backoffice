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
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidPrimaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.exception.InvalidSecondaryUserException;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import br.com.rede.ke.backoffice.conciliation.domain.repository.UserRepository;
import br.com.rede.ke.backoffice.conciliation.domain.validation.Validation;
import br.com.rede.ke.backoffice.util.Result;
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
     *
     * @param pvPermissionRepository
     *            Pv permission repository.
     * @param userRepository
     *            User repository.
     */
    public UserService(PvPermissionRepository pvPermissionRepository, UserRepository userRepository) {
        this.pvPermissionRepository = pvPermissionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Verifies if user has access.
     * @param user user.
     * @param pv pv.
     * @return Result.
     */
    public Result<User, String> hasAccess(User user, Pv pv) {
        Validation<User> hasAccess = (user1) -> {
            return pvPermissionRepository.findByUser(user1).stream()
                .filter(permission -> permission.permitAccess(pv))
                .findAny()
                .map(PvPermission::getUser)
                .map(Result::<User, String>success)
                .orElseGet(() ->
                    Result.failure(String.format("Usuário '%s' não tem acesso ao pv '%s'",
                    user1.getEmail(), pv.getCode())));
        };
        return hasAccess.validate(user);
    }

    /**
     * Gets the primary user.
     *
     * @param email
     *            the email
     * @return the primary user
     */
    public Optional<User> getPrimaryUser(String email) {
        Optional<User> primaryUserOpt = userRepository.findByEmail(email);
        primaryUserOpt.ifPresent(this::checkMustBePrimary);
        return primaryUserOpt;
    }

    /**
     * Gets or creates user email.
     *
     * @param email
     *            user email.
     * @return the primary user created or from database.
     */
    public User getOrCreatePrimaryUser(final String email) {
        return getPrimaryUser(email).orElseGet(() -> createPrimaryUser(email));
    }

    /**
     * Gets the secondary user for given primary user.
     *
     * @param primaryUser
     *            the primary user
     * @param secondaryUserEmail
     *            the secondary user email
     * @return the secondary user
     */
    public User getOrCreateSecondaryUserFor(User primaryUser, String secondaryUserEmail) {
        Optional<User> secondaryUserOpt = userRepository.findByEmail(secondaryUserEmail);

        checkMustBePrimary(primaryUser);

        if (!secondaryUserOpt.isPresent()) {
            return createSecondaryUserFor(primaryUser, secondaryUserEmail);
        }

        User secondaryUser = secondaryUserOpt.get();
        if (secondaryUser.isPrimary()) {
            throw new InvalidSecondaryUserException(secondaryUser);
        }

        if (!primaryUser.isPrimaryOf(secondaryUser)) {
            throw new InvalidSecondaryUserException(primaryUser, secondaryUser);
        }

        return secondaryUser;
    }

    /**
     * Creates the secondary user.
     *
     * @param primaryUser
     *            the primary user
     * @param secondaryUserEmail
     *            the secondary user email
     * @return the user
     */
    private User createSecondaryUserFor(User primaryUser, String secondaryUserEmail) {
        User secondaryUser = new User();
        secondaryUser.setEmail(secondaryUserEmail);
        secondaryUser.setPrimaryUser(primaryUser);
        return userRepository.save(secondaryUser);
    }

    /**
     * Checks if user is primary, otherwise throw exception.
     * @param user user to be checked.
     */
    private void checkMustBePrimary(User user) {
        if (!user.isPrimary()) {
            throw new InvalidPrimaryUserException(user);
        }
    }

    private User createPrimaryUser(String email) {
        User primaryUser = new User();
        primaryUser.setEmail(email);
        return userRepository.save(primaryUser);
    }
}
