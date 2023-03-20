package com.example.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class SessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionApplication.class, args);
    }

}
