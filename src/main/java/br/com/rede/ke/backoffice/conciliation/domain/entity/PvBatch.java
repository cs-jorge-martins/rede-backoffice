package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * The Class PvBatch.
 */
public class PvBatch {
    
    /** The sucessful pvs. */
    private List<Pv> sucessfulPvs;
    
    /** The failed pvs. */
    private List<Pv> failedPvs;
    
    public PvBatch(){
        this.sucessfulPvs = new ArrayList<>();
        this.failedPvs = new ArrayList<>();
    }
    
    /**
     * Gets the sucessful pvs.
     *
     * @return the sucessful pvs
     */
    public List<Pv> getSucessfulPvs() {
        return ImmutableList.copyOf(sucessfulPvs);
    }
    
    /**
     * Adds the sucessful pv.
     *
     * @param pv the pv
     */
    public void addSuccessfulPv(Pv pv) {
        this.sucessfulPvs.add(pv);
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
