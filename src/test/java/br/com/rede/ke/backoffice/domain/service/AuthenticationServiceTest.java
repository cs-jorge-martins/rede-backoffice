package br.com.rede.ke.backoffice.domain.service;

import br.com.rede.ke.backoffice.domain.entity.User;
import br.com.rede.ke.backoffice.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService service;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testAutenticate() {
        Mockito.when(userRepository.findByNameAndPassword(anyString(), anyString()))
            .thenReturn(Optional.of(new User()));

        boolean isAuthenticated = service.authenticate(new User("root", "pass"));
        assertThat(isAuthenticated, equalTo(true));
    }

}
