package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PvPermissionId implements Serializable {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "PV_ID")
    private Long pvId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPvId() {
        return pvId;
    }

    public void setPvId(Long pvId) {
        this.pvId = pvId;
    }
}
