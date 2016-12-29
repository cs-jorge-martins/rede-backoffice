package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER_PV")
public class PvPermission {

    @EmbeddedId
    private PvPermissionId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("pvId")
    private Pv pv;

    public PvPermissionId getId() {
        return id;
    }

    public void setId(PvPermissionId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pv getPv() {
        return pv;
    }

    public void setPv(Pv pv) {
        this.pv = pv;
    }
}
