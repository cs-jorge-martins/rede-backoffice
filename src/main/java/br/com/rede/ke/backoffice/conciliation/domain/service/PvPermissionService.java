package br.com.rede.ke.backoffice.conciliation.domain.service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvPermission;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvPermissionSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.not;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class PvPermissionService {

    private PvPermissionRepository repository;

    public PvPermissionService(PvPermissionRepository repository) {
        this.repository = repository;
    }

    public Page<PvPermission> findAllByAcquirerAndCodeAndEmail(Acquirer acquirer, String code, String email, Pageable pageable) {
        Specifications<PvPermission> spec = where(not(pvAcquirerEqualTo(Acquirer.REDE)));

        if (!isEmpty(code))
            spec = spec.and(pvCodeContains(code));

        if (!Acquirer.NULL.equals(acquirer))
            spec = spec.and(pvAcquirerEqualTo(acquirer));

        if (!isEmpty(email))
            spec = spec.and(userEmailContains(email));

        return repository.findAll(spec, pageable);
    }
}
