package br.com.rede.ke.backoffice.domain.repository;

import br.com.rede.ke.backoffice.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private User user;

    public UserRepository(User user) {
        this.user = user;
    }

    public Optional<User> findByName(String username) {
        if (user.getName().equals(username)) {
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
