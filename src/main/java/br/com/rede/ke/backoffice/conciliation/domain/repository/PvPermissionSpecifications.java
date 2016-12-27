package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import org.springframework.data.jpa.domain.Specification;

public final class PvPermissionSpecifications {

    public static final Specification<PvPermission> pvCodeContains(String code) {
        return (root, query, cb) -> cb.like(root.get("pv").get("code"), code + "%");
    }

    public static final Specification<PvPermission> pvAcquirerEqualTo(Acquirer acquirer) {
        return (root, query, cb) -> {
            int acquirerId = acquirer.ordinal();
            return cb.equal(root.get("pv").get("acquirerId"), acquirerId);
        };
    }

    public static final Specification<PvPermission> userEmailContains(String email) {
        return (root, query, cb) -> cb.like(root.get("user").get("email"), email + "%");
    }
}

