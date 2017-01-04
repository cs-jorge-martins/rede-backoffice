package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.List;

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
    
    public boolean isValidPvFormat(Pv pv) {
        return pv.getCode().matches("[0-9]{1,20}");
    }
    
    public PvBatch processPvBatch(List<Pv> pvs){
        PvBatch pvBatch = new PvBatch();
        
        for(Pv pv: pvs){
            if(! isValidPvFormat(pv)){
                pvBatch.addFailedPv(pv);
                continue;
            }
            
            Pv foundPv = pvRepository.findByCode(pv.getCode());
            if(foundPv == null){
                pvRepository.save(pv);
                pvBatch.addSuccessfulPv(pv);
            } else {
                if(! foundPv.isHeadquarter()){
                    pvBatch.addFailedPv(pv);
                }
            }
        }
        
        return pvBatch;
    }
}
