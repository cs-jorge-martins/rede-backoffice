package br.com.rede.ke.backoffice.conciliation.domain.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PV")
public class Pv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("HEADQUARTER_ID")
    private Pv headquarter;

    @Column(name = "ACQUIRER_ID")
    private Integer acquirerId;

    @ManyToMany
    @JoinTable(
        name = "USER_PV",
        joinColumns = @JoinColumn(name = "PV_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"))
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Integer getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(Integer acquirerId) {
        this.acquirerId = acquirerId;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Acquirer getAcquirer() {
        return Acquirer.fromId(getAcquirerId());
    }

    public boolean isHeadquarter() {
        return this.headquarter == null;
    }
}
