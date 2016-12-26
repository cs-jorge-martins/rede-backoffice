package br.com.rede.ke.backoffice.conciliation.domain.entity;

public enum Acquirer {
    NULL,
    REDE,
    CIELO;

    static Acquirer fromId(int id) {
        return Acquirer.values()[id];
    }
}
