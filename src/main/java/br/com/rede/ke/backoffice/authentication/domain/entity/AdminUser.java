package br.com.rede.ke.backoffice.authentication.domain.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security.user")
public class AdminUser {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
