package br.com.rede.ke.backoffice.authentication.domain.repository;

import org.springframework.stereotype.Repository;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;

import java.util.Optional;

@Repository
public class AdminUserRepository {

    private AdminUser adminUser;

    public AdminUserRepository(AdminUser user) {
        this.adminUser = user;
    }

    public Optional<AdminUser> findByName(String username) {
        if (adminUser.getName().equals(username)) {
            return Optional.of(adminUser);
        }

        return Optional.empty();
    }
}
