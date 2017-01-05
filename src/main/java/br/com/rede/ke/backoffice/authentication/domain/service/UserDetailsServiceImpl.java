package br.com.rede.ke.backoffice.authentication.domain.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rede.ke.backoffice.authentication.domain.entity.AdminUser;
import br.com.rede.ke.backoffice.authentication.domain.repository.AdminUserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private AdminUserRepository adminUserRepository;

    public UserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username or password invalid!"));
        return new User(adminUser.getName(), adminUser.getPassword(), Collections.emptyList());
    }
}
