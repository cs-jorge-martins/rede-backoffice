package br.com.rede.ke.backoffice.authentication.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;
import br.com.rede.ke.backoffice.authentication.domain.repository.AdminUserRepository;
import br.com.rede.ke.backoffice.authentication.domain.service.UserDetailsServiceImpl;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    private static final String VALID_USERNAME = "root";

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private AdminUserRepository userRepository;

    @Before
    public void setup() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByName(VALID_USERNAME)).thenReturn(Optional.of(createAdminUser()));
    }

    @Test
    public void testLoadUserByUsernameGivenValidUsername() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(VALID_USERNAME);
        assertThat(userDetails.getUsername(), equalTo(VALID_USERNAME));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameGivenInvalidUsername() {
        userDetailsService.loadUserByUsername("invalid_username");
    }

    private AdminUser createAdminUser() {
        AdminUser user = new AdminUser();
        user.setName(VALID_USERNAME);
        user.setPassword("password");
        return user;
    }
}