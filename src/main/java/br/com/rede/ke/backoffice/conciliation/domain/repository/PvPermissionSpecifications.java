package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public final class PvPermissionSpecifications {

    public static final Specification<PvPermission> pvCodeContains(String code) {
        return (root, query, cb) -> cb.like(root.join("pv", JoinType.LEFT).get("code"), code + "%");
    }

    public static final Specification<PvPermission> pvAcquirerEqualTo(Acquirer acquirer) {
        return (root, query, cb) -> {
            int acquirerId = acquirer.ordinal();
            return cb.equal(root.join("pv").get("acquirerId"), acquirerId);
        };
    }

    public static final Specification<PvPermission> userEmailContains(String email) {
        return (root, query, cb) -> cb.like(root.join("users", JoinType.LEFT).get("email"), email + "%");
    }
}

