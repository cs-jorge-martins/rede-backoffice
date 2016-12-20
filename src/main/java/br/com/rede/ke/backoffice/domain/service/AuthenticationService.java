package br.com.rede.ke.backoffice.domain.service;

import br.com.rede.ke.backoffice.domain.entity.User;
import br.com.rede.ke.backoffice.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(User user) {
        Optional<User> userFound = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
        return userFound.isPresent();
    }
}
