package br.com.rede.ke.backoffice.conciliation.domain.entity;

import java.util.List;

/**
 * The Class PvBatch.
 */
public class PvBatch {
    
    /** The sucessful pvs. */
    private List<Pv> sucessfulPvs;
    
    /** The failed pvs. */
    private List<Pv> failedPvs;
    
    /**
     * Gets the sucessful pvs.
     *
     * @return the sucessful pvs
     */
    public List<Pv> getSucessfulPvs() {
        return sucessfulPvs;
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
        return failedPvs;
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
