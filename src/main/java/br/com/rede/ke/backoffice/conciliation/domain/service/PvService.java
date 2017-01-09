package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.entity.PvBatch;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;

@Service
public class PvService {
    
    private PvRepository repository;
    
    public PvService(PvRepository pvRepository){
        this.repository = pvRepository;
    }
    
    public Pv save(Pv pv){
        return repository.save(pv);
    }
    
    public boolean isValidPvFormat(Pv pv) {
        return pv.getCode().matches("[0-9]{1,20}");
    }
    
    public PvBatch generatePvBatch(List<Pv> pvs){
        PvBatch pvBatch = new PvBatch();
        
        for(Pv pv: pvs){
            if(! isValidPvFormat(pv)){
                pvBatch.addInvalidPv(pv);
                continue;
            }
            
            Pv foundPv = repository.findByCode(pv.getCode());
            if(foundPv == null){
                pvBatch.addValidPv(pv);
            } else if(! foundPv.isHeadquarter()){
                pvBatch.addInvalidPv(pv);
            }
        }
        
        return pvBatch;
    }
}
