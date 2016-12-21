package br.com.rede.ke.backoffice.domain.repository;

import br.com.rede.ke.backoffice.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @Value("${security.user.name}")
    private String name;

    @Value("${security.user.password}")
    private String password;

    public Optional<User> findByName(String username) {
        User user = getUser();

        if (user.getName().equals(username)) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    private User getUser() {
        return new User(name, password);
    }

}
