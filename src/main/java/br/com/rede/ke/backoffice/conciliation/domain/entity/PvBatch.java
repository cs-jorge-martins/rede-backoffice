package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * The Class PvBatch.
 */
public class PvBatch {
    
    /** The successful pvs. */
    private List<Pv> successfulPvs;
    
    /** The failed pvs. */
    private List<Pv> failedPvs;
    
    public PvBatch(){
        this.successfulPvs = new ArrayList<>();
        this.failedPvs = new ArrayList<>();
    }
    
    /**
     * Gets the successful pvs.
     *
     * @return the successful pvs
     */
    public List<Pv> getSuccessfulPvs() {
        return ImmutableList.copyOf(successfulPvs);
    }
    
    /**
     * Adds the successful pv.
     *
     * @param pv the pv
     */
    public void addSuccessfulPv(Pv pv) {
        this.successfulPvs.add(pv);
    }
    
    /**
     * Gets the failed pvs.
     *
     * @return the failed pvs
     */
    public List<Pv> getFailedPvs() {
        return ImmutableList.copyOf(failedPvs);
    }
    
    /**
     * Adds the failed pv.
     *
     * @param pv the pv
     */
    public void addFailedPv(Pv pv) {
        this.failedPvs.add(pv);
    }
}
