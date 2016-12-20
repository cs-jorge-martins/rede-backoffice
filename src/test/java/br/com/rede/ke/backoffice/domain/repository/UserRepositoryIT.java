package br.com.rede.ke.backoffice.domain.repository;

import br.com.rede.ke.backoffice.domain.entity.User;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserRepositoryIT {

    private UserRepository repository;

    @Before
    public void init() {
        this.repository = new UserRepository();
    }

    @Test
    public void testFindByNameAndPassword() {
        Optional<User> user = repository.findByNameAndPassword("root", "pass");
        assertThat(user.isPresent(), equalTo(true));
    }
}