package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;

@Service
public class PvService {
    private PvRepository pvRepository;
    
    public PvService(PvRepository pvRepository){
        this.pvRepository = pvRepository;
    }
    
    public boolean isValidPv(Pv pv) {
        return pv.getCode().matches("\\d{1,20}");
    }
    
    public PvBatch savePvBatch(List<Pv> pvs){
        PvBatch pvBatch = new PvBatch();
        
        for(Pv pv: pvs){
            if(isValidPv(pv)){
                try {
                    pvRepository.save(pv);
                } catch (DataIntegrityViolationException e) {
                    pvBatch.addFailedPv(pv);
                }
                pvBatch.addSuccessfulPv(pv);
            } else {
                pvBatch.addFailedPv(pv);
            }
        }
        
        return pvBatch;
    }
}
