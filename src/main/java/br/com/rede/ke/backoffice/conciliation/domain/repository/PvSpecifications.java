package br.com.rede.ke.backoffice.conciliation.domain.repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import org.springframework.data.jpa.domain.Specification;

public final class PvSpecifications {

    public static final Specification<Pv> pvCodeEqualTo(String code) {
        return (root, query, cb) -> cb.like(root.get("code"), code + "%");
    }

    public static final Specification<Pv> pvAcquirerEqualTo(Acquirer acquirer) {
        return (root, query, cb) -> {
            int acquirerId = acquirer.ordinal();
            return cb.equal(root.get("acquirerId"), acquirerId);
        };
    }
}

