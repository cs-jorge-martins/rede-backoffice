package br.com.rede.ke.backoffice.conciliation.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;

@Repository
public interface PvRepository extends JpaRepository<Pv, Long>{

    List<Pv> findByCodeIn(List<String> sucessfulPvCodes);
    
    List<Pv> findByCodeAndHeadquarterIsNull(String code);

}
