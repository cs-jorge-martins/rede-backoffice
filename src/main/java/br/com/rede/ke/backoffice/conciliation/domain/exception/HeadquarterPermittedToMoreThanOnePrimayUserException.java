/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : HeadquarterPermittedToTwoDifferentPrimayUsers.java
 * Descrição: HeadquarterPermittedToTwoDifferentPrimayUsers.java.
 * Autor    : Geisly Conca <gconca@thoughtworks.com>
 * Data     : 29/01/2017
 * Empresa  : Thoughtworks
 */
package br.com.rede.ke.backoffice.conciliation.domain.exception;

import java.util.List;
import java.util.stream.Collectors;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.User;

/**
 * Class HeadquarterPermittedToTwoDifferentPrimayUsers.
 */
public class HeadquarterPermittedToMoreThanOnePrimayUserException extends DomainException {
    /**
     * The constructor.
     *
     * @param headquarter headquarter.
     * @param primayUsersPermitted primaryUsers
     */
    public HeadquarterPermittedToMoreThanOnePrimayUserException(Pv headquarter, List<User> primayUsersPermitted) {
        super(String.format("Há: %s usuários com permissão para o pv de código: %s quando só poderia haver 1. "
                + "Email dos usuários encontrados: %s",
            primayUsersPermitted.size(),
            headquarter.getCode(),
            primayUsersPermitted.stream().map(User::getEmail).collect(Collectors.joining(", "))));
    }
}
