package com.example.session.config;

import jakarta.servlet.FilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .cors()
                    .disable()
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register", "/registerOk").permitAll()
                        .requestMatchers("/setting/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                );

        return http.build();
    }


}
