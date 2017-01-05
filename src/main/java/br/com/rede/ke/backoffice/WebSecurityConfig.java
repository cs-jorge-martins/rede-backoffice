package br.com.rede.ke.backoffice;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import br.com.rede.ke.backoffice.domain.entity.User;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(User.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
            .formLogin()
                .loginPage("/auth/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }
}
