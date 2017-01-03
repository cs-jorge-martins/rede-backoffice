package br.com.rede.ke.backoffice.conciliation.domain.service;

import java.util.ArrayList;
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
    
    public boolean isValidPv(Pv pv) {
        return pv.getCode().matches("[0-9]{1,20}");
    }
    
    public PvBatch processPvBatch(List<Pv> pvs){
        PvBatch pvBatch = new PvBatch();
        
        for(Pv pv: pvs){
            if(isValidPv(pv)){
                pvRepository.save(pv);
                pvBatch.addSuccessfulPv(pv);
            } else {
                pvBatch.addFailedPv(pv);
            }
        }
        
        return pvBatch;
    }

    public List<Pv> readPvsFromString(String pvList) {
        List<Pv> pvs = new ArrayList<>();
        String[] pvCodes = pvList.split("\n");
        for(String pvCode: pvCodes){
            Pv pv = new Pv();
            pv.setCode(pvCode);
            pvs.add(pv);
        }
        return pvs;
    }

    public List<Pv> findByPvCodes(List<String> sucessfulPvCodes) {
        return pvRepository.findByCodeIn(sucessfulPvCodes);
    }

    public List<Pv> findHeadquarterPv(Pv pv) {
        return pvRepository.findByCodeAndHeadquarterIsNull(pv.getCode());
    }
}
