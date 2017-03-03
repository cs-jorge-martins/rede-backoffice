/*
 * Copyright 2017 Rede S.A.
 *************************************************************
 * Nome     : PvPermissionSpecifications.java
 * Descrição: PvPermissionSpecifications.java.
 * Autor    : Johnny Richard <jrichard@thoughtworks.com>
 * Data     : 04/01/2017
 * Empresa  : ThoughtWorks
 */
package br.com.rede.ke.backoffice.conciliation.domain.repository;

import org.springframework.data.jpa.domain.Specification;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;

/**
 * The Class PvPermissionSpecifications.
 */
public final class PvPermissionSpecifications {

    /**
     * Instantiates a new pv permission specifications.
     */
    private PvPermissionSpecifications() {
    }

    /**
     * Pv code contains.
     *
     * @param code
     *            the code
     * @return the specification
     */
    public static Specification<PvPermission> pvCodeContains(String code) {
        return (root, query, cb) -> cb.like(root.get("pv").get("code"), code + "%");
    }

    /**
     * Pv Headquarter Rede Code contains.
     *
     * @param pvHeadquarterRedeCode
     *            the code
     * @return the specification
     */
    public static Specification<PvPermission> pvHeadquarterRedeContains(String pvHeadquarterRedeCode) {
        return (root, query, cb) -> cb.like(root.get("pvHeadquarterRede").get("code"), pvHeadquarterRedeCode + "%");
    }

    /**
     * Pv acquirer equal to.
     *
     * @param acquirer
     *            the acquirer
     * @return the specification
     */
    public static Specification<PvPermission> pvAcquirerEqualTo(Acquirer acquirer) {
        return (root, query, cb) -> {
            int acquirerId = acquirer.ordinal();
            return cb.equal(root.get("pv").get("acquirerId"), acquirerId);
        };
    }

    /**
     * User email contains.
     *
     * @param email
     *            the email
     * @return the specification
     */
    public static Specification<PvPermission> userEmailContains(String email) {
        return (root, query, cb) -> cb.like(root.get("user").get("email"), email + "%");
    }
}
