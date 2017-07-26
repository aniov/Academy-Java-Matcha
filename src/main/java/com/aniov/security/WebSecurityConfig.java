package com.aniov.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * Configure Http Security
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DatabaseAuthenticationProvider databaseAuthenticationProvider;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationErrorHandler myAuthenticationErrorHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(databaseAuthenticationProvider).eraseCredentials(true);
    }

    /* Set up SessionRegistry used to access logged accounts*/
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                    .formLogin()
                        .loginPage("/login").permitAll()
                            .failureHandler(myAuthenticationErrorHandler)
                            .successHandler(myAuthenticationSuccessHandler)
                .and()
                    .authorizeRequests()
                        .antMatchers("/", "/login", "/register", "/activate", "/changepassword", "resetpassword")
                            .permitAll()
                .and()
                    .authorizeRequests()
                        .antMatchers("/main", "/logout", "/user**", "/user/**", "/user/profile**", "/profile**", "/settings**")
                    .authenticated()
                /* Configure so we have 1 session used in SessionRegistry*/
                .and()
                    .sessionManagement()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .sessionRegistry(sessionRegistry());
                // @formatter:on
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers("/js/*", "/css/*", "/img/*", "/font/*");
    }

}
