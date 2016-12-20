package br.com.rede.ke.backoffice.domain.repository;

import br.com.rede.ke.backoffice.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Repository
public class UserRepository {

    private Properties credentials = new Properties();

    public UserRepository() {
        try {
            credentials.load(getCredentialProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InputStream getCredentialProperties() {
        return getClass().getResourceAsStream("/credentials.properties");
    }

    public Optional<User> findByNameAndPassword(String name, String password) {
        String userName = credentials.getProperty("user.name");
        String userPassword = credentials.getProperty("user.password");

        if (userName.equals(name) && userPassword.equals(password)) {
            return Optional.of(new User(userName, userPassword));
        }

        return Optional.empty();
    }
}
