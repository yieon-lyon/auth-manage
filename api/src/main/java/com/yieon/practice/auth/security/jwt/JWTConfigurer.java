package com.yieon.practice.auth.security.jwt;

import com.yieon.practice.auth.security.WebAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final WebAuthenticationProvider webAuthenticationProvider;

    public JWTConfigurer(WebAuthenticationProvider webAuthenticationProvider) {
        this.webAuthenticationProvider = webAuthenticationProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        JWTFilter customFilter = new JWTFilter(webAuthenticationProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
