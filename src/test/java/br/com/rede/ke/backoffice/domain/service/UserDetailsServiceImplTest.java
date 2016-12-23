package br.com.rede.ke.backoffice.domain.service;

import br.com.rede.ke.backoffice.domain.entity.User;
import br.com.rede.ke.backoffice.domain.repository.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private static final String VALID_USER_NAME = "root";

    @Before
    public void setup() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByName(VALID_USER_NAME)).thenReturn(Optional.of(new User(VALID_USER_NAME, anyString())));
    }

    @Test
    public void testLoadUserByNameGivenValidName() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("root");
        assertThat(userDetails.getUsername(), equalTo("root"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByNameGivenInvalidName() {
        userDetailsService.loadUserByUsername("invalid_user");
    }
}