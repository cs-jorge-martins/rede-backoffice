package br.com.rede.ke.backoffice.conciliation.domain.service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Acquirer;
import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvSpecifications.pvAcquirerEqualTo;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvSpecifications.pvCodeEqualTo;
import static br.com.rede.ke.backoffice.conciliation.domain.repository.PvSpecifications.pvUsersEmailContains;
import static org.springframework.data.jpa.domain.Specifications.where;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class PvService {

    private PvRepository pvRepository;

    public PvService(PvRepository pvRepository) {
        this.pvRepository = pvRepository;
    }

    public Page<Pv> findAllByAcquirerAndCodeAndUserEmail(Acquirer acquirer, String code, String email, Pageable pageable) {
        Specifications<Pv> spec = where(pvCodeEqualTo(code));

        if (!Acquirer.NULL.equals(acquirer))
           spec = spec.and(pvAcquirerEqualTo(acquirer));

        if (!isEmpty(email))
           spec = spec.and(pvUsersEmailContains(email));

        return pvRepository.findAll(spec, pageable);
    }
}
